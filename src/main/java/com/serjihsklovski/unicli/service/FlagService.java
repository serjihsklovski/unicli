package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

/**
 * An interface to interact with method parameters annotated with the `@Flag`
 * annotation, and to get meta-information about a usage's flags.
 *
 * @see Flag
 */
public interface FlagService {

    /**
     * Filters the boolean/Boolean parameters of the method (the usage method
     * is intended) as they are considered as flags in the Unicli context.
     *
     * @param method a Unicli usage method
     * @return boolean/Boolean parameters
     */
    Stream<Parameter> getAllBooleanParametersOfMethod(Method method);

    /**
     * Returns `@Flag` annotations declared under the `@Usage`s `flags` property.
     *
     * @param usageMethod a Unicli usage method
     * @return Usage.flags() as a stream
     * @see com.serjihsklovski.unicli.annotation.Usage#flags()
     */
    Stream<Flag> getAllFlagsOfMethod(Method usageMethod);

    /**
     * Returns the flag name considering the following rules:
     *
     * - if the parameter is annotated with `@Flag` annotation, and its `name`
     * property is set (not empty), then the `name` property is returned;
     *
     * - if the parameter is annotated with `@Flag` annotation, and its `name`
     * property is not set (empty), and its `value` property is set (not empty),
     * then the `value` property is returned;
     *
     * - if the parameter is not annotated with `@Flag` annotation, and the
     * compiled Java byte code saves real parameter names, then its name is
     * being converted from CamelCase to hyphened-case and returned
     *
     * - otherwise a RuntimeException is being thrown.
     *
     * NOTE: Despite the parameter's type should be boolean/Boolean as it represents
     * flag, however there is no strict checking of it.
     *
     * @param flagParameter a flag parameter from a Unicli usage
     * @return a flag name
     */
    String getFlagNameOfParameter(Parameter flagParameter);

}
