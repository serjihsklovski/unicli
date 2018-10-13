package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Param;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParamServiceImplTest {

    private ParamServiceImpl paramService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        paramService = new ParamServiceImpl();
    }

    // parseParamValue tests

    @Test
    public void parseParamValue_supportString() {
        Object actual = paramService.parseParamValue("Test String", String.class);
        assertTrue(actual instanceof String);
        assertEquals("Test String", actual);
    }

    @Test
    public void parseParamValue_supportInt() {
        Object actual = paramService.parseParamValue(String.valueOf(Integer.MAX_VALUE), int.class);
        assertTrue(actual instanceof Integer);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportInteger() {
        Object actual = paramService.parseParamValue(String.valueOf(Integer.MAX_VALUE), Integer.class);
        assertTrue(actual instanceof Integer);
        assertEquals(Integer.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportDouble() {
        Object actual = paramService.parseParamValue(String.valueOf(Double.MAX_VALUE), double.class);
        assertTrue(actual instanceof Double);
        assertEquals(Double.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportDoubleWrapper() {
        Object actual = paramService.parseParamValue(String.valueOf(Double.MAX_VALUE), Double.class);
        assertTrue(actual instanceof Double);
        assertEquals(Double.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportLong() {
        Object actual = paramService.parseParamValue(String.valueOf(Long.MAX_VALUE), long.class);
        assertTrue(actual instanceof Long);
        assertEquals(Long.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportLongWrapper() {
        Object actual = paramService.parseParamValue(String.valueOf(Long.MAX_VALUE), Long.class);
        assertTrue(actual instanceof Long);
        assertEquals(Long.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportByte() {
        Object actual = paramService.parseParamValue(String.valueOf(Byte.MAX_VALUE), byte.class);
        assertTrue(actual instanceof Byte);
        assertEquals(Byte.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportByteWrapper() {
        Object actual = paramService.parseParamValue(String.valueOf(Byte.MAX_VALUE), Byte.class);
        assertTrue(actual instanceof Byte);
        assertEquals(Byte.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportShort() {
        Object actual = paramService.parseParamValue(String.valueOf(Short.MAX_VALUE), short.class);
        assertTrue(actual instanceof Short);
        assertEquals(Short.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_supportShortWrapper() {
        Object actual = paramService.parseParamValue(String.valueOf(Short.MAX_VALUE), Short.class);
        assertTrue(actual instanceof Short);
        assertEquals(Short.MAX_VALUE, actual);
    }

    @Test
    public void parseParamValue_unsupportedType_expectExceptionThrown() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Cannot parse \"a\" of `%s` type. This type is not supported.",
                Character.class.getCanonicalName()));
        paramService.parseParamValue("a", Character.class);
    }

    // getParamName / getParamNameOfParameter tests

    public void testUsage1(@Param(name = "aaa") int param) {
    }

    @Test
    public void getParamName_nameProperty() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage1", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertTrue(aaa.isAnnotationPresent(Param.class));
        assertEquals("aaa", paramService.getParamName(aaa.getAnnotation(Param.class)));
    }

    @Test
    public void getParamNameOfParameter_nameProperty() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage1", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", paramService.getParamNameOfParameter(aaa));
    }

    //

    public void testUsage2(@Param("aaa") int param) {
    }

    @Test
    public void getParamName_valueProperty() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage2", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertTrue(aaa.isAnnotationPresent(Param.class));
        assertEquals("aaa", paramService.getParamName(aaa.getAnnotation(Param.class)));
    }

    @Test
    public void getParamNameOfParameter_valueProperty() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage2", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter aaa = parameters[0];
        assertEquals("aaa", paramService.getParamNameOfParameter(aaa));
    }

    //

    public void testUsage3(@Param(value = "aaa", name = "bbb") int param) {
    }

    @Test
    public void getParamName_nameOverridesValue() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage3", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter bbb = parameters[0];
        assertTrue(bbb.isAnnotationPresent(Param.class));
        assertEquals("bbb", paramService.getParamName(bbb.getAnnotation(Param.class)));
    }

    @Test
    public void getParamNameOfParameter_nameOverridesValue() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage3", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter bbb = parameters[0];
        assertEquals("bbb", paramService.getParamNameOfParameter(bbb));
    }

    //

    public void testUsage4(@Param int namelessParam) {
    }

    @Test
    public void getParamName_namelessParam_expectExceptionThrown() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage4", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter namelessParam = parameters[0];
        assertTrue(namelessParam.isAnnotationPresent(Param.class));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The name of param is not set.");
        paramService.getParamName(namelessParam.getAnnotation(Param.class));
    }

    @Test
    public void getParamNameOfParameter_namelessParam_expectExceptionThrown() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage4", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter namelessParam = parameters[0];
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("The name of param is not set.");
        paramService.getParamNameOfParameter(namelessParam);
    }

    //

    public void testUsage5(int notParam) {
    }

    @Test
    public void getParamNameOfParameter_notParam_expectExceptionThrown() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage5", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notParam = parameters[0];
        assertFalse(notParam.isAnnotationPresent(Param.class));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format(
                "The parameter of type `%s` declared in the usage method should be annotated with the `%s` annotation. Usage method: `%s`.",
                int.class.getCanonicalName(), Param.class.getCanonicalName(), notParam.getDeclaringExecutable()));
        paramService.getParamNameOfParameter(notParam);
    }

    // isRequired / isNotRequired tests

    public void testUsage6(@Param int requiredParam) {
    }

    @Test
    public void isRequired_requiredByDefault() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage6", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter requiredParam = parameters[0];
        assertTrue(requiredParam.isAnnotationPresent(Param.class));
        assertTrue(paramService.isRequired(requiredParam));
    }

    @Test
    public void isNotRequired_requiredByDefault() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage6", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter requiredParam = parameters[0];
        assertTrue(requiredParam.isAnnotationPresent(Param.class));
        assertFalse(paramService.isNotRequired(requiredParam));
    }

    //

    public void testUsage7(@Param(required = false) int notRequiredParam) {
    }

    @Test
    public void isRequired_notRequired() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage7", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notRequiredParam = parameters[0];
        assertTrue(notRequiredParam.isAnnotationPresent(Param.class));
        assertFalse(paramService.isRequired(notRequiredParam));
    }

    @Test
    public void isNotRequired_notRequired() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage7", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notRequiredParam = parameters[0];
        assertTrue(notRequiredParam.isAnnotationPresent(Param.class));
        assertTrue(paramService.isNotRequired(notRequiredParam));
    }

    //

    public void testUsage8(int notParam) {
    }

    @Test
    public void isRequired_notParam_expectExceptionThrown() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage8", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notParam = parameters[0];
        assertFalse(notParam.isAnnotationPresent(Param.class));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format(
                "The parameter of type `%s` declared in the usage method should be annotated with the `%s` annotation. Usage method: `%s`.",
                notParam.getType().getCanonicalName(), Param.class.getCanonicalName(),
                notParam.getDeclaringExecutable()));
        paramService.isRequired(notParam);
    }

    @Test
    public void isNotRequired_notParam_expectExceptionThrown() throws NoSuchMethodException {
        Method method = ParamServiceImplTest.class.getMethod("testUsage8", int.class);
        Parameter[] parameters = method.getParameters();
        assertEquals(1, parameters.length);
        Parameter notParam = parameters[0];
        assertFalse(notParam.isAnnotationPresent(Param.class));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format(
                "The parameter of type `%s` declared in the usage method should be annotated with the `%s` annotation. Usage method: `%s`.",
                notParam.getType().getCanonicalName(), Param.class.getCanonicalName(),
                notParam.getDeclaringExecutable()));
        paramService.isNotRequired(notParam);
    }

}
