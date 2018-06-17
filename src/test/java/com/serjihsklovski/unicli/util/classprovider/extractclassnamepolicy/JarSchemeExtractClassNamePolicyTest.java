package com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JarSchemeExtractClassNamePolicyTest {

    @Test
    public void extractClassName_expectOk() {
        URI classPath = URI.create("file:///my/path/to/classpath.jar");
        URI classResourcePath = URI.create("jar:file:///my/path/to/classpath.jar!/com/someone/ClassExample.class");
        String expectedValue = "com.someone.ClassExample";

        ExtractClassNamePolicy extractClassNamePolicy = new JarSchemeExtractClassNamePolicy();
        String actualValue = extractClassNamePolicy.extractClassName(classPath, classResourcePath);

        assertThat(actualValue, is(expectedValue));
    }

    @Test(expected = RuntimeException.class)
    public void extractClassName_expectException_asWrongClasspath() {
        URI classPath = URI.create("file:///my/path/to/classpath.jar");
        URI classResourcePath = URI.create("jar:file:///wrong/path/to/classpath.jar!/com/someone/ClassExample.class");

        ExtractClassNamePolicy extractClassNamePolicy = new JarSchemeExtractClassNamePolicy();
        extractClassNamePolicy.extractClassName(classPath, classResourcePath);
    }

    @Test(expected = RuntimeException.class)
    public void extractClassName_expectException_asResourceNotClass() {
        URI classPath = URI.create("file:///my/path/to/classpath.jar");
        URI classResourcePath = URI.create("jar:file:///wrong/path/to/classpath.jar!/com/someone/Picture.png");

        ExtractClassNamePolicy extractClassNamePolicy = new JarSchemeExtractClassNamePolicy();
        extractClassNamePolicy.extractClassName(classPath, classResourcePath);
    }

}
