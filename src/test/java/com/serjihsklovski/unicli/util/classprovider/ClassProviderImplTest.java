package com.serjihsklovski.unicli.util.classprovider;

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
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.TestAnnotation.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.TestClass.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.TestEnum.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.TestInterface.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestAnnotation.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestClass.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestEnum.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestInterface.class
        ));
        String root1 = "com.serjihsklovski.unicli.util.classprovider.test.classprovider";
        testClassSet(classSet1, root1);

        Set<Class> classSet2 = new HashSet<>(Arrays.asList(
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestAnnotation.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestClass.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestEnum.class,
                com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage.TestInterface.class
        ));
        String root2 = "com.serjihsklovski.unicli.util.classprovider.test.classprovider.subpackage";
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
