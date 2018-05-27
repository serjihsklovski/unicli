package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.exception.TaskAnnotationMisuseException;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Stream;

public class TaskServiceImpl implements TaskService {

    private Set<Class> classPool;

    public TaskServiceImpl(Set<Class> classPool) {
        this.classPool = classPool;
    }

    @Override
    public Stream<Class> getAllTaskClasses() {
        return classPool.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Task.class))
                .peek(clazz -> {
                    if (!isPublicClass(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
                        // TODO: enable @Task for interfaces
                        throw new TaskAnnotationMisuseException(clazz);
                    }
                });
    }

    private boolean isPublicClass(Class clazz) {
        return !clazz.isEnum()
                && clazz.toString().startsWith("class")
                && Modifier.isPublic(clazz.getModifiers());
    }

}
