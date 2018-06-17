package com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy;

import java.net.URI;

/**
 * A policy to extract canonical class names.
 */
public interface ExtractClassNamePolicy {

    /**
     * Conforming to the URI slashed notation.
     * An example of URI:
     * `file:/path/to/resource`
     */
    String URI_SEPARATOR = "/";

    /**
     * Conforming to the Java's dotted notation.
     * A canonical class name example:
     * `com.someone.ClassExample`
     */
    String CLASS_SEPARATOR = ".";

    /**
     * Returns the canonical class name extracted from
     * the `resourcePath`. The `resourcePath` must be
     * a URI to the .class file existing on the disk.
     * To extract the canonical class name, the classpath
     * should be specified. Give the `classPath` parameter
     * for this purpose.
     *
     * @param classPath a classpath URI
     * @param resourcePath a resource URI to a .class file
     * @return a canonical class name
     */
    String extractClassName(URI classPath, URI resourcePath);

}
