package com.serjihsklovski.unicli.util.classprovider;

import com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy.ExtractClassNamePolicy;
import com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy.FileSchemeExtractClassNamePolicy;
import com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy.JarSchemeExtractClassNamePolicy;
import com.serjihsklovski.unicli.util.classprovider.findresourcespolicy.FileSchemeFindResourcesPolicy;
import com.serjihsklovski.unicli.util.classprovider.findresourcespolicy.FindResourcesPolicy;
import com.serjihsklovski.unicli.util.classprovider.findresourcespolicy.JarSchemeFindResourcesPolicy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassProviderImpl implements ClassProvider {

    private static final String SCHEME_JAR = "jar";

    private static final String SCHEME_FILE = "file";

    private static final Set<String> EXCLUDED_PACKAGES = new HashSet<>();

    private static final String GROUP_FILE_PATH = "filePath";

    private static final String TEMPLATE_FILE_PATH_URI = String.format("^file:/*(?<%s>.*)", GROUP_FILE_PATH);

    private static final String TEMPLATE_CLASS_FILE = ".*\\.class$";

    static {
        EXCLUDED_PACKAGES.add("com.sun.");
    }

    @Override
    public Set<Class> fetchAllClassesByRoots(Set<String> roots) {
        return roots.stream()
                .map(this::findClassesByPackageName)
                .flatMap(classes -> classes)
                .collect(Collectors.toSet());
    }

    private Stream<? extends Class<?>> findClassesByPackageName(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<URI> resourcesRoots = getResourcesRoots(classLoader, packageName);
        Set<URI> classPaths = getClassPaths(classLoader);
        Map<URI, Set<URI>> classPathToResources = groupResourcesByClassPath(resourcesRoots, classPaths);

        return classPathToResources.entrySet().stream()
                .map(entry -> getClassesCanonicalNames(entry.getKey(), entry.getValue()))
                .flatMap(classesCanonicalNames -> classesCanonicalNames)
                .filter(this::notExcludedClass)
                .filter(this::notInnerClass)
                .distinct()
                .map(this::findClassForName)
                .filter(Objects::nonNull);
    }

    private Set<URI> getResourcesRoots(ClassLoader classLoader, String packageName) {
        String packageNamePath = packageName.replace(ExtractClassNamePolicy.CLASS_SEPARATOR,
                ExtractClassNamePolicy.URI_SEPARATOR);

        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(packageNamePath);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        Set<URI> resourcesRoots = new HashSet<>();
        while (resources.hasMoreElements()) {
            resourcesRoots.add(convertUrlToUri(resources.nextElement()));
        }
        return resourcesRoots;
    }

    private Set<URI> getClassPaths(ClassLoader classLoader) {
        return Stream.of(((URLClassLoader) classLoader).getURLs())
                .map(this::convertUrlToUri)
                .map(Paths::get)
                .map(Path::toUri)
                .map(URI::normalize)
                .collect(Collectors.toSet());
    }

    private Map<URI, Set<URI>> groupResourcesByClassPath(Set<URI> resourcesRoots, Set<URI> submittedClassPaths) {
        Map<URI, Set<URI>> classPathToResources = new HashMap<>();
        resourcesRoots.stream()
                .map(this::getAllResources)
                .forEach(resourcesGroup -> {
                    if (resourcesGroup.size() == 0) {
                        throw new RuntimeException("No resources found for the class path.");
                    }
                    URI typicalGroupUri = resourcesGroup.stream().findFirst().get();
                    URI classPath = getResourceClassPath(typicalGroupUri, submittedClassPaths);
                    classPathToResources.put(classPath, resourcesGroup);
                });

        return classPathToResources;
    }

    private URI getResourceClassPath(URI resourceUri, Set<URI> submittedClassPaths) {
        if (resourceUri.getScheme().equals(SCHEME_JAR)) {
            // emulate `file` scheme behaviour
            JarSchemeUriExtractor uriExtractor = JarSchemeUriExtractor.extract(resourceUri);
            resourceUri = URI.create(String.format("%s%s%s", uriExtractor.getJarFileUri().toString(),
                    ExtractClassNamePolicy.URI_SEPARATOR, uriExtractor.getJarEntryName()));
        }

        return findResourceClassPath(resourceUri, submittedClassPaths);
    }

    private URI findResourceClassPath(URI resourcePath, Set<URI> submittedClassPaths) {
        String resourcePathString = getFilePath(resourcePath);
        return submittedClassPaths.stream()
                .filter(classPath -> resourcePathString.startsWith(getFilePath(classPath)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find classpath for the resource `%s`.",
                        resourcePath)));
    }

    private String getFilePath(URI filePathUri) {
        Pattern pattern = Pattern.compile(TEMPLATE_FILE_PATH_URI);
        Matcher matcher = pattern.matcher(filePathUri.toString());
        if (!matcher.matches()) {
            throw new RuntimeException("The `filePathUri` does not match to the file path template.");
        }
        return matcher.group(GROUP_FILE_PATH);
    }

    private URI convertUrlToUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException use) {
            throw new RuntimeException(use);
        }
    }

    private Set<URI> getAllResources(URI resourcesRoot) {
        return getFindResourcesPolicy(resourcesRoot.getScheme()).findResources(resourcesRoot);
    }

    private Stream<String> getClassesCanonicalNames(URI classPath, Set<URI> resources) {
        URI typicalGroupUri = resources.stream().findFirst().orElseThrow(() -> new RuntimeException(String.format(
                "Unexpected error occurred: no resources found for the class path `%s`.", classPath)));

        ExtractClassNamePolicy extractClassNamePolicy = getExtractClassNamePolicy(typicalGroupUri.getScheme());

        return resources.stream()
                .filter(resource -> Pattern.compile(TEMPLATE_CLASS_FILE).asPredicate().test(resource.toString()))
                .map(resource -> extractClassNamePolicy.extractClassName(classPath, resource));
    }

    private FindResourcesPolicy getFindResourcesPolicy(String uriScheme) {
        switch (uriScheme) {
            case SCHEME_JAR:
                return new JarSchemeFindResourcesPolicy();

            case SCHEME_FILE:
                return new FileSchemeFindResourcesPolicy();

            default:
                throw new RuntimeException(String.format(
                        "Cannot find `FindResourcesPolicy` implementation for `%s` URI scheme.", uriScheme));
        }
    }

    private ExtractClassNamePolicy getExtractClassNamePolicy(String uriScheme) {
        switch (uriScheme) {
            case SCHEME_JAR:
                return new JarSchemeExtractClassNamePolicy();

            case SCHEME_FILE:
                return new FileSchemeExtractClassNamePolicy();

            default:
                throw new RuntimeException(String.format(
                        "Cannot find `ExtractClassNamePolicy` implementation for `%s` URI scheme.", uriScheme));
        }
    }

    private Class<?> findClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        } catch (NoClassDefFoundError ncdfe) {
            return null;    // filter nullable results in the top method
        }
    }

    private boolean notInnerClass(String className) {
        return !className.contains("$");
    }

    private boolean notExcludedClass(String className) {
        return EXCLUDED_PACKAGES.stream()
                .noneMatch(className::startsWith);
    }

}
