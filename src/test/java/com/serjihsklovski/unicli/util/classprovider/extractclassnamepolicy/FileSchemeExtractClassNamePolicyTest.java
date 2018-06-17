package com.serjihsklovski.unicli.util.classprovider.extractclassnamepolicy;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FileSchemeExtractClassNamePolicyTest {

    @Test
    public void extractClassName_expectOk() {
        URI classPath = URI.create("file:///my/path/to/classpath/");
        URI classResourcePath = URI.create("file:///my/path/to/classpath/com/someone/ClassExample.class");
        String expectedValue = "com.someone.ClassExample";

        ExtractClassNamePolicy extractClassNamePolicy = new FileSchemeExtractClassNamePolicy();
        String actualValue = extractClassNamePolicy.extractClassName(classPath, classResourcePath);

        assertThat(actualValue, is(expectedValue));
    }

    @Test(expected = RuntimeException.class)
    public void extractClassName_expectException_asClassPathNotMatches() {
        URI classPath = URI.create("error:///my/wrong/path/to/classpath/");
        URI classResourcePath = URI.create("file:///my/path/to/classpath/com/someone/ClassExample.class");

        ExtractClassNamePolicy extractClassNamePolicy = new FileSchemeExtractClassNamePolicy();
        extractClassNamePolicy.extractClassName(classPath, classResourcePath);
    }

    @Test(expected = RuntimeException.class)
    public void extractClassName_expectException_asResourceNotMatches() {
        URI classPath = URI.create("file:///my/path/to/classpath/");
        URI classResourcePath = URI.create("file:///my/path/to/classpath/com/someone/Picture.png");

        ExtractClassNamePolicy extractClassNamePolicy = new FileSchemeExtractClassNamePolicy();
        extractClassNamePolicy.extractClassName(classPath, classResourcePath);
    }

    @Test(expected = RuntimeException.class)
    public void extractClassName_expectException_asResourceIsNotSubpath() {
        URI classPath = URI.create("file:///my/path/to/classpath/");
        URI classResourcePath = URI.create("file:///my/wrong/path/to/classpath/com/someone/ClassExample.class");

        ExtractClassNamePolicy extractClassNamePolicy = new FileSchemeExtractClassNamePolicy();
        extractClassNamePolicy.extractClassName(classPath, classResourcePath);
    }

}
