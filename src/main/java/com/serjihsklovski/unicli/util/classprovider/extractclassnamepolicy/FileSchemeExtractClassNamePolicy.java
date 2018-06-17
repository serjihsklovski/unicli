package com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A client should use this implementation of `ExtractClassNamePolicy` interface
 * with `file` scheme resources URIs.
 */
public class FileSchemeExtractClassNamePolicy implements ExtractClassNamePolicy {

    private static final String GROUP_CLASS_PATH = "classPath";

    private static final String GROUP_RESOURCE_PATH = "resourcePath";

    private static final String GROUP_CLASS_NAME = "className";

    private static final String TEMPLATE_CLASS_PATH_URI = String.format("^file:/*(?<%s>.*)", GROUP_CLASS_PATH);

    private static final String TEMPLATE_CLASS_RESOURCE_URI =
            String.format("^file:/*(?<%s>.*)\\.class$", GROUP_RESOURCE_PATH);

    /**
     * Usage example:
     * - given `classPath`: a URI like `file:///my/class/path/`
     * - given `resourcePath`: a URI like `file:///my/class/path/package/to/my/ClassFile.class`
     * - returned canonical class name: "package.to.my.ClassFile"
     *
     * @param classPath a classpath URI
     * @param resourcePath a resource URI to a .class file
     * @return a canonical class name
     */
    @Override
    public String extractClassName(URI classPath, URI resourcePath) {
        String classPathWithoutScheme = getClassPathWithoutScheme(classPath);
        String resourcePathWithoutSchemeAndExtension = getResourcePathWithoutSchemeAndExtension(resourcePath);
        String className = cutOffClassPath(resourcePathWithoutSchemeAndExtension, classPathWithoutScheme);
        return className.replace(URI_SEPARATOR, CLASS_SEPARATOR);
    }

    private String getClassPathWithoutScheme(URI classPath) {
        Pattern pattern = Pattern.compile(TEMPLATE_CLASS_PATH_URI);
        Matcher matcher = pattern.matcher(classPath.toString());
        if (!matcher.matches()) {
            throw new RuntimeException(
                    "Unexpected error occurred: the `classPath` does not match to the class path URI template.");
        }
        return matcher.group(GROUP_CLASS_PATH);
    }

    private String getResourcePathWithoutSchemeAndExtension(URI resourcePath) {
        Pattern pattern = Pattern.compile(TEMPLATE_CLASS_RESOURCE_URI);
        Matcher matcher = pattern.matcher(resourcePath.toString());
        if (!matcher.matches()) {
            throw new RuntimeException(
                    "Unexpected error occurred: the `resourcePath` does not match to the .class filename template.");
        }
        return matcher.group(GROUP_RESOURCE_PATH);
    }

    private String cutOffClassPath(String resourcePath, String classPath) {
        String template = String.format("^%s/?(?<%s>.*)", classPath, GROUP_CLASS_NAME);
        Pattern pattern = Pattern.compile(template);
        Matcher matcher = pattern.matcher(resourcePath);
        if (!matcher.matches()) {
            throw new RuntimeException(
                    "Unexpected error occurred: the `resourcePath` is not subpath of the `classPath`.");
        }
        return matcher.group(GROUP_CLASS_NAME);
    }

}
