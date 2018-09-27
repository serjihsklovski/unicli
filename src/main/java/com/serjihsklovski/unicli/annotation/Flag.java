package com.serjihsklovski.unicli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flags affect which functionality should be enabled or disabled for
 * a particular task. To give a flag to the task, provide the flag name
 * prepended with two dashes (e.g. `--verbose` for the flag with name `verbose`).
 *
 * The flags can be considered by usage methods either as its boolean parameters,
 * or as the `flags`-enumeration in the `@Usage` annotation.
 * @see Usage#flags()
 *
 * You are required to annotate boolean (Boolean) parameters with the `@Flag`
 * annotation and specify their names. But, this is possible to make `@Flag`
 * annotations optional. To do so, you need to add the Java compiler option
 * `-parameters` for your application build, so the compiler will save the real
 * names of the method parameters in the Java byte code instead of replaced ones
 * (arg0, arg1, ...) by default. The CamelCase names of the usage methods'
 * parameters will be visible to a parser as the hyphened analogs.
 * @see Flag#name()
 * For the `-parameters` javac standard option see at
 * {@link <a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javac.html">the Oracle javac docs</a>}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Flag {

    /**
     * Alias to `name`.
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
