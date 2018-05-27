package com.serjihsklovski.unicli.exception;

public class NonTaskClassException extends RuntimeException {

    public NonTaskClassException(Class clazz) {
        super(createMessage(clazz));
    }

    private static String createMessage(Class clazz) {
        return String.format("Class `%s` is not a Unicli task.", clazz.getCanonicalName());
    }

}
