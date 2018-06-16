package com.serjihsklovski.unicli.util.classprovider.findresourcespolicy;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A client should use this implementation of `FindResourcesPolicy` interface
 * with `file` scheme URIs.
 */
public class FileSchemeFindResourcesPolicy implements FindResourcesPolicy {

    /**
     * Example usage:
     * - given root: a URI like `file:///C:/path/to/resources/`
     * - returned set: [
     *   `file:///C:/path/to/resources/resource.txt`,
     *   `file:///C:/path/to/resources/subdir/resource.txt`
     * ]
     *
     * @param root URI to start searching
     * @return a set of files representing resources
     */
    @Override
    public Set<URI> findResources(URI root) {
        try {
            return Files.walk(Paths.get(root))
                    .filter(path -> !Files.isDirectory(path))
                    .map(Path::toUri)
                    .collect(Collectors.toSet());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
