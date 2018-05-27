package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;
import com.serjihsklovski.unicli.exception.NonTaskClassException;
import com.serjihsklovski.unicli.exception.UsageAnnotationMisuseException;
import com.serjihsklovski.unicli.exception.UsageMethodTakesParamsException;
import com.serjihsklovski.unicli.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class UsageServiceImpl implements UsageService {

    @Override
    public Stream<Method> getAllUsagesByTaskClass(Class taskClass) {
        if (!taskClass.isAnnotationPresent(Task.class)) {
            throw new NonTaskClassException(taskClass);
        }

        return Stream.of(taskClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Usage.class))
                .peek(method -> {
                    if (!ReflectionUtils.isPublic(method) || ReflectionUtils.isStatic(method)
                            || ReflectionUtils.isAbstract(method) || !ReflectionUtils.returnsVoid(method)) {
                        // TODO: enable abstract Usage methods in interfaces
                        throw new UsageAnnotationMisuseException(method);
                    }
                    if (!takesNoParams(method)) {   // TODO: enable parametrized Usage methods
                        throw new UsageMethodTakesParamsException(method);
                    }
                });
    }

    private boolean takesNoParams(Method method) {
        return method.getParameterCount() == 0;
    }

}
