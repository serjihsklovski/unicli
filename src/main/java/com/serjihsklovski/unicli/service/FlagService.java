package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

/**
 * An interface to interact with method parameters annotated with the `@Flag`
 * annotation, and to get meta-information about a usage's flags.
 * @see Flag
 */
public interface FlagService {

    /**
     * Filters the `boolean` / `Boolean` parameters of the method (the usage
     * method is intended) as they are considered as flags in the Unicli
     * context.
     *
     * @param method a Unicli usage method
     * @return `boolean` / `Boolean` parameters
     */
    Stream<Parameter> getAllBooleanParametersOfMethod(Method method);

    /**
     * Returns `@Flag` annotations declared under the `@Usage`s `flags`
     * property.
     * @see com.serjihsklovski.unicli.annotation.Usage#flags()
     *
     * @param usageMethod a Unicli usage method
     * @return Usage.flags() as a stream
     */
    Stream<Flag> getAllFlagsOfMethod(Method usageMethod);

    /**
     * Returns the flag name considering the following rules:
     *
     * - if the `flag`'s `name` property is set (not empty), then the `name`
     * property is returned;
     *
     * - if the `flag`'s `name` property is not set (empty), and its `value`
     * property is set (not empty), then the `value` property is returned;
     *
     * - otherwise a `RuntimeException` is being thrown.
     *
     * @param flag meta information about the flag whose name you want to get
     * @return a flag name
     * @throws RuntimeException if the flag's name is not set none of the ways
     */
    String getFlagName(Flag flag);

    /**
     * Retrieves the `@Flag` annotation from the `flagParameter` and gets the
     * flag name from it, accessing `getFlagName` method. If the `@Flag`
     * annotation does not present in the `flagParameter`, then a
     * `RuntimeException` is being thrown.
     * @see FlagService#getFlagName(Flag)
     *
     * NOTE: Despite it is implies that the parameter's type is `boolean` or
     * `Boolean` (as it represents a flag), however there is no strict checking
     * of it.
     *
     * @param flagParameter a flag parameter from a Unicli usage
     * @return a flag name
     * @throws RuntimeException if the `flagParameter` is not annotated with
     *                          the `@Flag` annotation
     */
    String getFlagNameOfParameter(Parameter flagParameter);

}
