package com.serjihsklovski.unicli.util.classprovider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassProviderImpl implements ClassProvider {

    private static final Boolean FILES = false;

    private static final Boolean DIRS = true;

    @Override
    public Set<Class> fetchAllClassesByRoots(Set<String> roots) {
        return roots.stream()
                .map(this::findClassesByPackageName)
                .flatMap(classes -> classes)
                .collect(Collectors.toSet());
    }

    private Stream<Class> findClassesByPackageName(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = getResources(classLoader, packageName);
        Collection<String> directories = getDirectories(resources);
        return getClasses(directories, packageName);
    }

    private static Enumeration<URL> getResources(ClassLoader classLoader, String packageName) {
        String packageNamePath = packageName.replace('.', '/');
        try {
            return classLoader.getResources(packageNamePath);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private Collection<String> getDirectories(Enumeration<URL> resources) {
        Collection<String> directories = new HashSet<>();
        while (resources.hasMoreElements()) {
            try {
                directories.add(Paths.get(resources.nextElement().toURI()).toString());
            } catch (URISyntaxException use) {
                throw new RuntimeException(use);
            }
        }
        return directories;
    }

    private Stream<Class> getClasses(Collection<String> directories, String packageName) {
        return directories.stream()
                .map(dir -> Paths.get(dir))
                .filter(dir -> Files.exists(dir))
                .map(this::findFilesRecursively)
                .flatMap(filesStream -> filesStream)
                .filter(this::isClassFile)
                .map(classFilePath -> trimToClassName(classFilePath, packageName))
                .map(this::findClassForName);
    }

    private Stream<Path> findFilesRecursively(Path path) {
        if (Files.isDirectory(path)) {
            try {
                Map<Boolean, List<Path>> filesAndDirs = Files.list(path)
                        .collect(Collectors.groupingBy(p -> Files.isDirectory(p)));

                if (filesAndDirs.containsKey(FILES) && filesAndDirs.containsKey(DIRS)) {
                    return Stream.concat(
                            filesAndDirs.get(FILES).stream(),
                            filesAndDirs.get(DIRS).stream()
                                    .map(this::findFilesRecursively)
                                    .flatMap(files -> files)
                    );
                } else if (filesAndDirs.containsKey(FILES)) {
                    return filesAndDirs.get(FILES).stream();
                } else if (filesAndDirs.containsKey(DIRS)) {
                    return filesAndDirs.get(DIRS).stream()
                            .map(this::findFilesRecursively)
                            .flatMap(files -> files);
                } else {
                    return Stream.empty();
                }

            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        return Stream.of(path);
    }

    private boolean isClassFile(Path path) {
        return path.toString().endsWith(".class");
    }

    private String trimToClassName(Path classFilePath, String packageName) {
        String packageNamePath = packageName.replace(".", "\\" + File.separator);
        Pattern packagePattern = Pattern.compile(String.format("(.*)(%s.*)\\.class", packageNamePath));
        Matcher matcher = packagePattern.matcher(classFilePath.toString());

        final int CLASS_NAME_GROUP = 2;
        if (matcher.matches()) {
            return matcher.group(CLASS_NAME_GROUP).replace(File.separator, ".");
        }
        throw new RuntimeException(String.format("Cannot find a package name `%s`", packageName));
    }

    private Class<?> findClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }

}
