package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Usage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlagServiceImplTest {

    private FlagService flagService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        flagService = new FlagServiceImpl();
    }

    //

    public void testUsage1(Boolean b1, boolean b2, String s) {
    }

    @Test
    public void getAllBooleanParametersOfMethodTest() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage1", Boolean.class, boolean.class, String.class);
        Set<Parameter> flags = flagService.getAllBooleanParametersOfMethod(method).collect(Collectors.toSet());
        assertEquals(2, flags.size());
    }

    //

    @Usage(flags = {
            @Flag("aaa"),
            @Flag("bbb")
    })
    public void testUsage2() {
    }

    @Test
    public void getAllFlagsOfMethodTest() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage2");
        Set<Flag> flags = flagService.getAllFlagsOfMethod(method).collect(Collectors.toSet());
        assertEquals(2, flags.size());
    }

    //

    public void testUsage3(@Flag(name = "aaa") boolean flag) {
    }

    @Test
    public void getFlagName_nameProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage3", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertTrue(aaa.isAnnotationPresent(Flag.class));
        assertEquals("aaa", flagService.getFlagName(aaa.getAnnotation(Flag.class)));
    }

    @Test
    public void getFlagNameOfParameter_nameProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage3", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", flagService.getFlagNameOfParameter(aaa));
    }

    //

    public void testUsage4(@Flag("aaa") boolean flag) {
    }

    @Test
    public void getFlagName_valueProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage4", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertTrue(aaa.isAnnotationPresent(Flag.class));
        assertEquals("aaa", flagService.getFlagName(aaa.getAnnotation(Flag.class)));
    }

    @Test
    public void getFlagNameOfParameter_valueProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage4", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", flagService.getFlagNameOfParameter(aaa));
    }

    //

    public void testUsage5(@Flag(value = "aaa", name = "bbb") boolean flag) {
    }

    @Test
    public void getFlagName_nameOverridesValue() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage5", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter bbb = parameters[0];
        assertTrue(bbb.isAnnotationPresent(Flag.class));
        assertEquals("bbb", flagService.getFlagName(bbb.getAnnotation(Flag.class)));
    }

    @Test
    public void getFlagNameOfParameter_nameOverridesValue() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage5", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter bbb = parameters[0];
        assertEquals("bbb", flagService.getFlagNameOfParameter(bbb));
    }

    //

    public void testUsage6(@Flag boolean namelessFlag) {
    }

    @Test
    public void getFlagName_namelessFlag_expectExceptionThrown() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage6", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter namelessFlag = parameters[0];
        namelessFlag.isAnnotationPresent(Flag.class);
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Flag's name is not set.");
        flagService.getFlagName(namelessFlag.getAnnotation(Flag.class));
    }

    @Test
    public void getFlagNameOfParameter_namelessFlag_expectExceptionThrown() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage6", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter namelessFlag = parameters[0];
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Flag's name is not set.");
        flagService.getFlagNameOfParameter(namelessFlag);
    }

    //

    public void testUsage7(boolean notFlag) {
    }

    @Test
    public void getFlagNameOfParameter_notFlag_expectExceptionThrown() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage7", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notFlag = parameters[0];
        assertFalse(notFlag.isAnnotationPresent(Flag.class));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format(
                "The `%s` instance is not annotated with the `%s` annotation. Method: `%s`.",
                Parameter.class.getCanonicalName(), Flag.class.getCanonicalName(), notFlag.getDeclaringExecutable()));
        flagService.getFlagNameOfParameter(notFlag);
    }

}
