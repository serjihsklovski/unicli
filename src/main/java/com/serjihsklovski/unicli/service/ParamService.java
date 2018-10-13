package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.stream.Stream;

/**
 * An interface to interact with the method parameters annotated with the
 * {@code @Param} annotation, and to get metadata about usage's params.
 * @see Param
 */
public interface ParamService {

    /**
     * @return a set of all supported types that could be parsed
     */
    Set<Class> getSupportedParamTypes();

    /**
     * Parses {@code paramValue} string according to the {@code paramType}
     * class that should be supported for parsing. To find out if the service
     * supports parsing the given type of values, the method checks if it is in
     * the set of supported param types. If no, then a {@code RuntimeException}
     * is being thrown.
     * @see ParamService#getSupportedParamTypes()
     *
     * The return object can be passed as an argument to the method call using
     * Java Reflection API.
     * @see java.lang.reflect.Method#invoke(Object, Object...)
     *
     * @param paramValue the value that have to be parsed
     * @param paramType the class whose instance is being created and returned
     * @return an object value that is the instance of {@code paramType} and
     *         parsed from the {@code paramValue} string
     * @throws RuntimeException if the given {@code paramType} class is not
     *                          supported for parsing
     */
    Object parseParamValue(String paramValue, Class paramType);

    /**
     * Returns the param name considering the following rules:
     *
     * - if the {@code param}'s {@code name} property is set (not empty), then
     * the {@code name} property is returned;
     *
     * - if the {@code param}'s {@code name} property is not set (empty), and
     * its {@code value} property is set (not empty), then the {@code value}
     * property is returned;
     *
     * - otherwise a {@code RuntimeException} is being thrown.
     *
     * @param param metadata about the param whose name you want to get
     * @return the param name
     * @throws RuntimeException if the param's name is not set none of the ways
     */
    String getParamName(Param param);

    /**
     * Retrieves the {@code @Param} annotation from the {@code parameter} and
     * gets the param name of it, accessing {@code getParamName} method. If the
     * {@code @Param} annotation does not present in the {@code parameter},
     * then {@code RuntimeException} is being thrown.
     * @see ParamService#getParamName(Param)
     *
     * NOTE: There is no strict checking whether the param type is supported or
     * not.
     *
     * @param parameter a parameter from a Unicli usage method
     * @return the param name
     * @throws RuntimeException if the {@code parameter} is not annotated with
     *                          the {@code @Param} annotation
     */
    String getParamNameOfParameter(Parameter parameter);

    /**
     * Retrieves the {@code @Param} annotation from the {@code parameter} and
     * gets the {@code required} property from it. If the {@code Param}
     * annotation does not present in the {@code parameter}, then a
     * {@code RuntimeException} is being thrown.
     *
     * @param parameter a parameter from a Unicli usage method
     * @return {@code required} property from the {@code @Param} annotation
     * retrieved
     * @throws RuntimeException if the {@code parameter} is not annotated with
     *                          the {@code @Param} annotation
     */
    boolean isRequired(Parameter parameter);

    /**
     * @param parameter a parameter from a Unicli usage method
     * @return the negated result of the {@code isRequired} method
     * @see ParamService#isRequired(Parameter)
     */
    boolean isNotRequired(Parameter parameter);

    /**
     * Takes all the parameters of the {@code method} argument and streams them
     * filtering by theirs type support.
     *
     * NOTE: There is no checking whether the {@code method}'s parameters are
     * annotated with the {@code @Param} annotation or not.
     *
     * @param method a Unicli usage method
     * @return parameters whose type is supported
     */
    Stream<Parameter> getAllSupportedParametersOfMethod(Method method);

}
