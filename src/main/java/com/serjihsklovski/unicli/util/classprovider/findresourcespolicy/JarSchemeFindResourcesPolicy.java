package com.serjihsklovski.unicli.util.classprovider.findresourcespolicy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * A client should use this implementation of `FindResourcesPolicy` interface
 * with `jar` scheme URIs.
 */
public class JarSchemeFindResourcesPolicy implements FindResourcesPolicy {

    private static final String JAR_PATH_DELIMITER = "!/";

    /**
     * Example usage:
     * - given root: a URI like `jar:file:///C:/path/to/jar/file.jar!/path/to/resources/`
     * - returned set: [
     *   `jar:file:///C:/path/to/jar/file.jar!/path/to/resources/resource.class`,
     *   `jar:file:///C:/path/to/jar/file.jar!/path/to/resources/subdir/resource.class`
     * ]
     *
     * @param root URI to start searching
     * @return a set of files representing resources
     */
    @Override
    public Set<URI> findResources(URI root) {
        JarSchemeUriExtractor uriExtractor = JarSchemeUriExtractor.extract(root);
        URI classPathUri = uriExtractor.getJarFileUri();
        String resourcePath = uriExtractor.getJarEntryName();

        Set<URI> result = new HashSet<>();
        Path jarFileLocation = Paths.get(classPathUri);
        try (JarInputStream zipInputStream = new JarInputStream(Files.newInputStream(jarFileLocation))) {
            JarEntry jarEntry;
            while ((jarEntry = zipInputStream.getNextJarEntry()) != null) {
                if (jarEntry.isDirectory()) {
                    continue;
                }

                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(resourcePath)) {
                    result.add(createJarSchemeUri(classPathUri, jarEntryName));
                }
            }

            return result;
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(String.format("Cannot find a .jar file at location `%s`", jarFileLocation), fnfe);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private URI createJarSchemeUri(URI classPathUri, String jarEntryName) {
        return URI.create(String.format("jar:%s%s%s", classPathUri, JAR_PATH_DELIMITER, jarEntryName));
    }

}
