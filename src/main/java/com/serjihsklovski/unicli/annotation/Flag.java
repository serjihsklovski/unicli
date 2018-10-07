package com.serjihsklovski.unicli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flags affect which functionality should be enabled or disabled for a
 * particular task. To give a flag to the task, provide the flag name prepended
 * with two dashes (e.g. `--verbose` for the flag with name `verbose`).
 *
 * The flags can be considered by usage methods either as its `boolean`
 * parameters, or as the `flags`-enumeration in the `@Usage` annotation.
 * @see Usage#flags()
 *
 * You are required to annotate `boolean` (`Boolean`) parameters with the
 * `@Flag` annotation and specify their names.
 * @see Flag#name()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Flag {

    /**
     * Alias to the `name` property.
     * @see Flag#name()
     */
    String value() default "";

    /**
     * In CLI, the name of a flag is indicated after two dashes. For example,
     * if the flag has the name `verbose`, then you specify `--verbose` in the
     * CLI calls.
     *
     * The `name` property is required.
     */
    String name() default "";

}
