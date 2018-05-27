package com.serjihsklovski.unicli.exception;

import com.serjihsklovski.unicli.util.ReflectionUtils;

import java.lang.reflect.Method;

public class UsageAnnotationMisuseException extends RuntimeException {

    public UsageAnnotationMisuseException(Method method) {
        super(createMessage(method));
    }

    private static String createMessage(Method method) {
        if (!ReflectionUtils.isPublic(method)) {
            return String.format("Usage method `%s` should be defined as public.", method.getName());
        }

        if (ReflectionUtils.isStatic(method)) {
            return String.format("Usage method `%s` should be defined as non-static.", method.getName());
        }

        if (ReflectionUtils.isAbstract(method)) {
            return String.format("Usage method `%s` should be defined as non-abstract.", method.getName());
        }

        if (!ReflectionUtils.returnsVoid(method)) {
            return String.format("Usage method `%s` should return void.", method.getName());
        }

        return String.format("Usage method `%s` is defined wrong.", method.getName());
    }

}
