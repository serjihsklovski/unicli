package com.serjihsklovski.unicli.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaseUtilsTest {

    @Test
    public void convertCamelCaseToHyphenedLowercaseAsserts() {
        assertEquals("aa-aa-aaa", CaseUtils.convertCamelCaseToHyphenedLowercase("aaAAAaa"));
        assertEquals("aa-aaaa", CaseUtils.convertCamelCaseToHyphenedLowercase("aaAaaa"));
        assertEquals("camel-case", CaseUtils.convertCamelCaseToHyphenedLowercase("camelCase"));
        assertEquals("camel-case", CaseUtils.convertCamelCaseToHyphenedLowercase("CamelCase"));
        assertEquals("camel-case-a", CaseUtils.convertCamelCaseToHyphenedLowercase("CamelCaseA"));
        assertEquals("lower-camel-case", CaseUtils.convertCamelCaseToHyphenedLowercase("lowerCamelCase"));
        assertEquals("a-beautiful-lie", CaseUtils.convertCamelCaseToHyphenedLowercase("ABeautifulLie"));
        assertEquals("camel444-case500", CaseUtils.convertCamelCaseToHyphenedLowercase("camel444Case500"));
        assertEquals("html", CaseUtils.convertCamelCaseToHyphenedLowercase("HTML"));
        assertEquals("a-html", CaseUtils.convertCamelCaseToHyphenedLowercase("aHTML"));
        assertEquals("url-encoded", CaseUtils.convertCamelCaseToHyphenedLowercase("URLEncoded"));
    }

}
