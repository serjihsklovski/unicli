package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Usage;
import com.serjihsklovski.unicli.util.CaseUtils;

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
    public String getFlagNameOfParameter(Parameter flagParameter) {
        if (flagParameter.isAnnotationPresent(Flag.class)) {
            Flag flag = flagParameter.getAnnotation(Flag.class);
            if (!flag.name().isEmpty()) {
                return flag.name();
            } else if (!flag.value().isEmpty()) {
                return flag.value();
            }
        }

        if (flagParameter.isNamePresent()) {
            return CaseUtils.convertCamelCaseToHyphenedLowercase(flagParameter.getName());
        }

        throw new RuntimeException("The Java compiler do not save real names of method parameters in the byte code " +
                "by default. If you want the Java compiler to save the real names of the parameters, please add the " +
                "`-parameters` flag calling `javac`.");
    }

}
