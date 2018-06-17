package com.serjihsklovski.unicli.util.classprovider.findresourcespolicy;

import java.net.URI;
import java.util.Set;

/**
 * A policy to find any resources using catalog URIs.
 */
public interface FindResourcesPolicy {

    /**
     * Finds all existing resources in the root URI and its subpaths.
     * The returning set does not contain directories.
     * If the `root` parameter is an existing file URI,
     * then the returning set contains only this file.
     *
     * @param root a URI to start searching
     * @return a set of files representing resources
     */
    Set<URI> findResources(URI root);

}
