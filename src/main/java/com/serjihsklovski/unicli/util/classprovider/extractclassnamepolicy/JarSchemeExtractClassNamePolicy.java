package com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy;

import com.serjihsklovski.unicli.util.classprovider.JarSchemeUriExtractor;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A client should use this implementation of `ExtractClassNamePolicy` interface
 * with `jar` scheme URIs.
 */
public class JarSchemeExtractClassNamePolicy implements ExtractClassNamePolicy {

    private static final String GROUP_CLASS_NAME = "className";

    private static final String TEMPLATE_CLASS_NAME_JAR_ENTRY = String.format("(?<%s>.*)\\.class$", GROUP_CLASS_NAME);

    /**
     * Usage example:
     * - given `classPath`: a URI like `file:///my/class/path.jar`
     * - given `resourcePath`: a URI like `jar:file:///my/class/path.jar!/package/to/my/ClassFile.class`
     * - returned canonical class name: "package.to.my.ClassFile"
     *
     * @param classPath a classpath URI
     * @param resourcePath a resource URI to a .class file
     * @return a canonical class name
     */
    @Override
    public String extractClassName(URI classPath, URI resourcePath) {
        JarSchemeUriExtractor uriExtractor = JarSchemeUriExtractor.extract(resourcePath);
        if (uriExtractor.getJarFileUri().compareTo(classPath) != 0) {
            throw new RuntimeException(
                    "Unexpected error occurred: the `classPath` is not the same as in the `resourcePath`.");
        }
        String className = cutOffExtension(uriExtractor.getJarEntryName());
        return className.replace(URI_SEPARATOR, CLASS_SEPARATOR);
    }

    private String cutOffExtension(String jarEntryName) {
        Pattern pattern = Pattern.compile(TEMPLATE_CLASS_NAME_JAR_ENTRY);
        Matcher matcher = pattern.matcher(jarEntryName);
        matcher.matches();  // always matches as the `JarSchemeUriExtractor` avoids wrong URIs
        return matcher.group(GROUP_CLASS_NAME);
    }

}
