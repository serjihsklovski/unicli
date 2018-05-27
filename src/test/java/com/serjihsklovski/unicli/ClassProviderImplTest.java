package com.serjihsklovski.unicli;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ClassProviderImplTest {

    private static final String PACKAGE_CONTAINS_CLASSES_MSG_TEMPLATE = "Package `%s` contains %d classes.";

    private static final String CLASS_IS_MISSING_MSG_TEMPLATE = "Class `%s` is missing, although it should be.";

    @Test
    public void fetchAllClassesByRoots() {
        Set<Class> classSet1 = new HashSet<>(Arrays.asList(
                com.serjihsklovski.unicli.test.TestAnnotation.class,
                com.serjihsklovski.unicli.test.TestClass.class,
                com.serjihsklovski.unicli.test.TestEnum.class,
                com.serjihsklovski.unicli.test.TestInterface.class,
                com.serjihsklovski.unicli.test.subpackage.TestAnnotation.class,
                com.serjihsklovski.unicli.test.subpackage.TestClass.class,
                com.serjihsklovski.unicli.test.subpackage.TestEnum.class,
                com.serjihsklovski.unicli.test.subpackage.TestInterface.class
        ));
        String root1 = "com.serjihsklovski.unicli.test";
        testClassSet(classSet1, root1);

        Set<Class> classSet2 = new HashSet<>(Arrays.asList(
                com.serjihsklovski.unicli.test.subpackage.TestAnnotation.class,
                com.serjihsklovski.unicli.test.subpackage.TestClass.class,
                com.serjihsklovski.unicli.test.subpackage.TestEnum.class,
                com.serjihsklovski.unicli.test.subpackage.TestInterface.class
        ));
        String root2 = "com.serjihsklovski.unicli.test.subpackage";
        testClassSet(classSet2, root2);
    }

    private void testClassSet(Set<Class> classSet, String root) {
        ClassProvider classProvider = new ClassProviderImpl();
        Set<Class> testClasses = classProvider.fetchAllClassesByRoots(Collections.singleton(root));
        int expectedValue = classSet.size();
        assertThat(
                String.format(PACKAGE_CONTAINS_CLASSES_MSG_TEMPLATE, root, expectedValue),
                testClasses.size(),
                is(expectedValue)
        );
        testClasses.forEach(clazz ->
                assertTrue(
                        String.format(CLASS_IS_MISSING_MSG_TEMPLATE, clazz.getCanonicalName()),
                        testClasses.contains(clazz)
                )
        );
    }

}
