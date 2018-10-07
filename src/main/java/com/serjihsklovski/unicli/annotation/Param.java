package com.serjihsklovski.unicli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameters (params) allows you to provide named and valuable arguments to a
 * task. You can declare parameters of different types. The following types are
 * supported:
 * - `String`
 * - `int` / `Integer`
 * - `double` / `Double`
 * - `long` / `Long`
 * - `byte` / `Byte`
 * - `short` / `Short`
 *
 * To declare a param, annotate a method parameter with the `@Param`
 * annotation, and then specify a name of the param through the `name` or
 * `value` property.
 * @see Param#name()
 * @see Param#value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * Alias to the `name` property.
     * @see Param#name()
     */
    String value() default "";

    /**
     * In CLI, the name of a param is indicated after two dashes. The argument
     * value goes after the "equals" character. For example, if the parameter
     * has the name `language` and the value `en-US`, then you specify
     * `--language=en-US` in the CLI calls.
     *
     * The `name` property is required.
     */
    String name() default "";

    /**
     * If `true`, then the param must be specified in the CLI call for a
     * particular usage. Otherwise, the `defaultValue` is used to be parsed
     * when the param is not specified in the CLI call.
     * @see Param#defaultValue()
     */
    boolean required() default true;

    /**
     * This property is used as the default argument value if the non required
     * param is not specified in the CLI call. The parameter parses the
     * `defaultValue` as the source only in the case of `required` property is
     * set to `false`.
     * @see Param#required()
     *
     * NOTE: By default, this property is set as the empty string, but for not
     * all the types it could be parsed as the meaningful value.
     */
    String defaultValue() default "";

}
