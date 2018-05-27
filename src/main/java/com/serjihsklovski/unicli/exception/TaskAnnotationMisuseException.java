package com.serjihsklovski.unicli.exception;

import com.serjihsklovski.unicli.annotation.Task;

public class TaskAnnotationMisuseException extends RuntimeException {

    public TaskAnnotationMisuseException(Class clazz) {
        super(createMessage(clazz));
    }

    private static String createMessage(Class clazz) {
        if (clazz.isAnnotation()) {
            return String.format(
                    "The `%s` annotation is not completable to annotation classes, but the annotation class `%s` is annotated with it.",
                    Task.class.getCanonicalName(), clazz.getCanonicalName()
            );
        }

        if (clazz.isEnum()) {
            return String.format(
                    "The `%s` annotation is not completable to enumeration classes, but the enumeration class `%s` is annotated with it.",
                    Task.class.getCanonicalName(), clazz.getCanonicalName()
            );
        }

        return String.format(
                "The `%s` annotation is not completable to this class type, but the class `%s` is annotated with it.",
                Task.class.getCanonicalName(), clazz.getCanonicalName()
        );
    }

}
