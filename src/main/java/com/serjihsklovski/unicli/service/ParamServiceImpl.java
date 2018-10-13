package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class ParamServiceImpl implements ParamService {

    private static final Map<Class, Function<String, Object>> SUPPORTED_PARAM_TYPES = new HashMap<>();

    static {
        // *type* -> *parser from `String` to `Object`*
        SUPPORTED_PARAM_TYPES.put(String.class, x -> x);
        SUPPORTED_PARAM_TYPES.put(int.class, Integer::parseInt);
        SUPPORTED_PARAM_TYPES.put(Integer.class, Integer::parseInt);
        SUPPORTED_PARAM_TYPES.put(double.class, Double::parseDouble);
        SUPPORTED_PARAM_TYPES.put(Double.class, Double::parseDouble);
        SUPPORTED_PARAM_TYPES.put(long.class, Long::parseLong);
        SUPPORTED_PARAM_TYPES.put(Long.class, Long::parseLong);
        SUPPORTED_PARAM_TYPES.put(byte.class, Byte::parseByte);
        SUPPORTED_PARAM_TYPES.put(Byte.class, Byte::parseByte);
        SUPPORTED_PARAM_TYPES.put(short.class, Short::parseShort);
        SUPPORTED_PARAM_TYPES.put(Short.class, Short::parseShort);
    }

    @Override
    public Set<Class> getSupportedParamTypes() {
        return SUPPORTED_PARAM_TYPES.keySet();
    }

    @Override
    public Object parseParamValue(String paramValue, Class paramType) {
        return Optional.ofNullable(SUPPORTED_PARAM_TYPES.get(paramType))
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot parse \"%s\" of `%s` type. This type is not supported.", paramValue,
                                paramType.getCanonicalName())))
                .apply(paramValue);
    }

    @Override
    public String getParamName(Param param) {
        if (!param.name().isEmpty()) {
            return param.name();
        } else if (!param.value().isEmpty()) {
            return param.value();
        }
        throw new RuntimeException("The name of param is not set.");
    }

    @Override
    public String getParamNameOfParameter(Parameter parameter) {
        if (parameter.isAnnotationPresent(Param.class)) {
            return getParamName(parameter.getAnnotation(Param.class));
        }
        throw new RuntimeException(String.format(
                "The parameter of type `%s` declared in the usage method should be annotated with the `%s` annotation. Usage method: `%s`.",
                parameter.getType().getCanonicalName(), Param.class.getCanonicalName(),
                parameter.getDeclaringExecutable()));
    }

    @Override
    public boolean isRequired(Parameter parameter) {
        if (parameter.isAnnotationPresent(Param.class)) {
            return parameter.getAnnotation(Param.class).required();
        }
        throw new RuntimeException(String.format(
                "The parameter of type `%s` declared in the usage method should be annotated with the `%s` annotation. Usage method: `%s`.",
                parameter.getType().getCanonicalName(), Param.class.getCanonicalName(),
                parameter.getDeclaringExecutable()));
    }

    @Override
    public boolean isNotRequired(Parameter parameter) {
        return !isRequired(parameter);
    }

    @Override
    public Stream<Parameter> getAllSupportedParametersOfMethod(Method method) {
        return Stream.of(method.getParameters())
                .filter(parameter -> getSupportedParamTypes().contains(parameter.getType()));
    }

}
