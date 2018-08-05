package com.serjihsklovski.unicli.engine.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ParameterizedTaskWrapper implements Supplier<Runnable> {

    private final Class taskClass;

    private Method usage;

    ParameterizedTaskWrapper(Class taskClass) {
        this.taskClass = taskClass;
    }

    Class getTaskClass() {
        return taskClass;
    }

    public void setUsage(Method usage) {
        this.usage = usage;
    }

    @Override
    public Runnable get() {
        Object task;
        try {
            task = taskClass.newInstance();
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(String.format("Cannot create an instance of the task class `%s`: access denied.",
                    taskClass.getCanonicalName()), iae);
        } catch (InstantiationException ie) {
            throw new RuntimeException(String.format("Cannot create an instance of a task class `%s`.",
                    taskClass.getCanonicalName()), ie);
        }

        return () -> {
            try {
                usage.invoke(task);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(String.format("Cannot invoke the method `%s`: access denied.",
                        usage.getName()), iae);
            } catch (InvocationTargetException ite) {
                throw new RuntimeException(ite);
            }
        };
    }

}
