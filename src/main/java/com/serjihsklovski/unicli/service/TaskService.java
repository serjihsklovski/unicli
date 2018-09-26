package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * An interface to interact with the classes, annotated
 * by the `@Task` annotation.
 * @see Task
 */
public interface TaskService {

    /**
     * Looks for all classes in the Unicli Class Pool that annotated with @Task.
     *
     * @return Unicli tasks
     */
    Stream<Class> getAllTaskClasses();

    /**
     * Finds the task with a specified name in the Unicli Class Pool.
     * @see Task#name()
     *
     * @param name a Unicli task name
     * @return the Unicli task class
     */
    Optional<Class> getTaskByName(String name);

    /**
     * Finds the root task in the Unicli Class Pool.
     * @see Task#root()
     *
     * @return the Unicli root task class
     */
    Optional<Class> getRootTask();

    /**
     * Returns the flag name considering the following rules:
     *
     * - if the class is annotated with `@Task` annotation, and its `name`
     *   property is set (not empty), then the `name` property is returned;
     *
     * - if the class is annotated with `@Task` annotation, and its `name`
     *   property is not set (empty), and its `value` property is set (not empty),
     *   then the `value` property is returned;
     *
     * - if the class is annotated with `@Task` annotation, and its properties
     *   `name` or `value` are both not set (empty), and the `root` property is `true`,
     *   then the empty `Optional` object is returned.
     *
     * - if either the class is not annotated with `@Task` annotation, or all previous
     *   rules are not met, then the `RuntimeException` is thrown.
     *
     * @param clazz a Unicli task class
     * @return optional task name
     */
    Optional<String> getTaskNameByClass(Class clazz);

}
