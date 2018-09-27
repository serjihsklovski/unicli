package com.serjihsklovski.unicli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Usage defines an action (set of actions) that a Unicli task can perform.
 * `@Usage` should annotate public non-abstract methods that return void.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Usage {

    /**
     * You can strictly specify a list (a set) of flags that will match this
     * usage method, unless you need more flexible boolean parameters. Also
     * you can combine the flags declared with this property with the boolean
     * parameters.
     */
    Flag[] flags() default {};

}
