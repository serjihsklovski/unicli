package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Usage;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FlagServiceImplTest {

    private FlagService flagService;

    @Before
    public void setup() {
        flagService = new FlagServiceImpl();
    }

    @Test
    public void getAllBooleanParametersOfMethodTest() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage1", Boolean.class, boolean.class, String.class);
        Set<Parameter> flags = flagService.getAllBooleanParametersOfMethod(method).collect(Collectors.toSet());
        assertEquals(2, flags.size());
    }

    public void testUsage1(Boolean b1, boolean b2, String s) {
    }

    @Test
    public void getAllFlagsOfMethodTest() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage2");
        Set<Flag> flags = flagService.getAllFlagsOfMethod(method).collect(Collectors.toSet());
        assertEquals(2, flags.size());
    }

    @Usage(flags = {
            @Flag("aaa"),
            @Flag("bbb")
    })
    public void testUsage2() {
    }

    @Test
    public void getFlagNameOfParameter_nameProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage3", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", flagService.getFlagNameOfParameter(aaa));
    }

    public void testUsage3(@Flag(name = "aaa") boolean flag) {
    }

    @Test
    public void getFlagNameOfParameter_valueProperty() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage4", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", flagService.getFlagNameOfParameter(aaa));
    }

    public void testUsage4(@Flag("aaa") boolean flag) {
    }

    @Test
    public void getFlagNameOfParameter_nameOverridesValue() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage5", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter bbb = parameters[0];
        assertEquals("bbb", flagService.getFlagNameOfParameter(bbb));
    }

    public void testUsage5(@Flag(value = "aaa", name = "bbb") boolean flag) {
    }

    @Test
    public void getFlagNameOfParameter_realNameOrException() throws NoSuchMethodException {
        Method method = FlagServiceImplTest.class.getMethod("testUsage6", boolean.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter flag = parameters[0];
        String parameterName;
        try {
            parameterName = flagService.getFlagNameOfParameter(flag);
        } catch (RuntimeException re) {
            String message = re.getMessage();
            assertTrue(message.startsWith("The Java compiler do not save real names of method parameters"));
            return;
        }
        assertEquals("flag", parameterName);
    }

    public void testUsage6(boolean flag) {
    }

}
