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

}
