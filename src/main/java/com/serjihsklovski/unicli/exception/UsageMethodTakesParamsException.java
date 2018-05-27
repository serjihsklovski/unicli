package com.serjihsklovski.unicli.exception;

import java.lang.reflect.Method;

public class UsageMethodTakesParamsException extends RuntimeException {

    public UsageMethodTakesParamsException(Method method) {
        super(createMessage(method));
    }

    private static String createMessage(Method method) {
        return String.format("Usage method `%s` should not take parameters.", method.getName());
    }

}
