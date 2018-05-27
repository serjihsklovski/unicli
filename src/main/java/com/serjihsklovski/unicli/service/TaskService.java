package com.serjihsklovski.unicli.service;

import java.util.stream.Stream;

public interface TaskService {

    /**
     * Looks for all classes in the Unicli Class Pool that annotated with @Task.
     *
     * @return Unicli tasks
     * @see com.serjihsklovski.unicli.annotation.Task
     */
    Stream<Class> getAllTaskClasses();

}
