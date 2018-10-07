package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Usage;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

public class FlagServiceImpl implements FlagService {

    @Override
    public Stream<Parameter> getAllBooleanParametersOfMethod(Method method) {
        return Stream.of(method.getParameters())
                .filter(parameter -> parameter.getType().isAssignableFrom(boolean.class) ||
                        parameter.getType().isAssignableFrom(Boolean.class));
    }

    @Override
    public Stream<Flag> getAllFlagsOfMethod(Method usageMethod) {
        return Stream.of(usageMethod.getAnnotation(Usage.class).flags());
    }

    @Override
    public String getFlagName(Flag flag) {
        if (!flag.name().isEmpty()) {
            return flag.name();
        } else if (!flag.value().isEmpty()) {
            return flag.value();
        }
        throw new RuntimeException("Flag's name is not set.");
    }

    @Override
    public String getFlagNameOfParameter(Parameter flagParameter) {
        if (flagParameter.isAnnotationPresent(Flag.class)) {
            return getFlagName(flagParameter.getAnnotation(Flag.class));
        }
        throw new RuntimeException(String.format(
                "The `%s` instance is not annotated with the `%s` annotation. Method: `%s`.",
                Parameter.class.getCanonicalName(), Flag.class.getCanonicalName(),
                flagParameter.getDeclaringExecutable()));
    }

}
