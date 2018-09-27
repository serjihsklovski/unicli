package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.exception.TaskAnnotationMisuseException;

import java.lang.reflect.Modifier;
import java.util.Optional;
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

    @Override
    public Optional<Class> getTaskByName(String name) {
        return getAllTaskClasses()
                .filter(clazz -> {
                    Task taskMeta = (Task) clazz.getAnnotation(Task.class);
                    if (!taskMeta.name().isEmpty()) {
                        return taskMeta.name().equals(name);
                    }
                    return taskMeta.value().equals(name);
                })
                .findFirst();
    }

    @Override
    public Optional<Class> getRootTask() {
        return getAllTaskClasses()
                .filter(clazz -> ((Task) clazz.getDeclaredAnnotation(Task.class)).root())
                .findFirst();
    }

    @Override
    public Optional<String> getTaskNameByClass(Class clazz) {
        if (!clazz.isAnnotationPresent(Task.class)) {
            throw new RuntimeException(String.format(
                    "Class `%s` is not the Unicli task class (it is not annotated with `%s` annotation).",
                    clazz.getCanonicalName(), Task.class.getCanonicalName()));
        }

        Task task = (Task) clazz.getAnnotation(Task.class);
        if (!task.name().isEmpty()) {
            return Optional.of(task.name());
        } else if (!task.value().isEmpty()) {
            return Optional.of(task.value());
        } else if (task.root()) {
            return Optional.empty();
        }

        throw new RuntimeException(String.format("Unicli task class `%s` has no name declared.",
                clazz.getCanonicalName()));
    }

    private boolean isPublicClass(Class clazz) {
        return !clazz.isEnum()
                && clazz.toString().startsWith("class")
                && Modifier.isPublic(clazz.getModifiers());
    }

}
