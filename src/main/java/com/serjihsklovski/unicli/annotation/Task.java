package com.serjihsklovski.unicli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A class annotated with `@Task` defines a Unicli task.
 *
 * Unicli tasks are the actions that your CLI application performs. For
 * example, it can be disk operations, or the business processes automating, or
 * some another concrete actions that cover your problems domain.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Task {

    /**
     * Alias to the `name` property.
     * @see Task#name()
     */
    String value() default "";

    /**
     * The name of the Unicli task with which you can call this task through
     * your CLI. For example, if you specified the name as "install", then you
     * could call it like this:
     *
     * `$ java -jar your-cli.jar install`
     *
     * The name must be specified if the task is not the root task.
     * @see Task#root()
     *
     * You can use the name's alias (`value`)
     * @see Task#value()
     */
    String name() default "";

    /**
     * Should this Unicli task be the root task?
     *
     * A root task is the Unicli task that can be invoked without specifying
     * its name. Example:
     *
     * `$ java -jar my-cli.jar root-task --param=value`
     * `$ java -jar my-cli.jar --param=value`
     *
     * You do need to specify a name for a root task.
     * @see Task#name()
     *
     * NOTE: only one root task should be defined!
     */
    boolean root() default false;

}
