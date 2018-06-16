package com.serjihsklovski.unicli.util.classprovider.findresourcespolicy;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JarSchemeUriExtractorTest {

    @Test
    public void extractRequiredUriFormat() {
        URI jarFileUri = URI.create("file:///path/to/jar/file.jar");
        String entryName = "com/someone";

        URI jarSchemeUri = URI.create(String.format("jar:%s!/%s", jarFileUri, entryName));
        JarSchemeUriExtractor uriExtractor = JarSchemeUriExtractor.extract(jarSchemeUri);

        int result1 = uriExtractor.getJarFileUri().compareTo(jarFileUri);
        assertThat(result1, is(0));

        boolean result2 = uriExtractor.getJarEntryName().equals(entryName);
        assertThat(result2, is(true));
    }

    @Test(expected = RuntimeException.class)
    public void extractWrongUriFormat() {
        URI wrongSchemeUri = URI.create("file:///path/to/jar/file.jar!/com/someone");
        JarSchemeUriExtractor.extract(wrongSchemeUri);
    }

}
