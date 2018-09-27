package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;
import com.serjihsklovski.unicli.exception.NonTaskClassException;
import com.serjihsklovski.unicli.exception.UsageAnnotationMisuseException;
import com.serjihsklovski.unicli.util.ReflectionUtils;
import com.serjihsklovski.unicli.util.set.SetOperations;
import com.serjihsklovski.unicli.util.set.SymmetricDifference;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsageServiceImpl implements UsageService {

    private final FlagService flagService;

    public UsageServiceImpl(FlagService flagService) {
        this.flagService = flagService;
    }

    @Override
    public Stream<Method> getAllUsagesByTaskClass(Class taskClass) {
        if (!taskClass.isAnnotationPresent(Task.class)) {
            throw new NonTaskClassException(taskClass);
        }

        return Stream.of(taskClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Usage.class))
                .peek(method -> {
                    if (!ReflectionUtils.isPublic(method) || ReflectionUtils.isAbstract(method) ||
                            !ReflectionUtils.returnsVoid(method)) {
                        // TODO: enable abstract Usage methods in interfaces
                        throw new UsageAnnotationMisuseException(method);
                    }
                });
    }

    @Override
    public Optional<Method> getUsageByTaskClassAndFlagNames(Class taskClass, Set<String> flagNames) {
        return getAllUsagesByTaskClass(taskClass)
                .map(method -> new Pair<>(method, getCliFlagsWithoutUsageFlags(method, flagNames)))
                .filter(pair -> pair.getValue().isPresent())
                .filter(pair -> allCliFlagsInMethodParams(pair.getKey(), pair.getValue().get()))
                .map(Pair::getKey)
                .findFirst();
    }

    private Optional<Set<String>> getCliFlagsWithoutUsageFlags(Method usageMethod, Set<String> cliFlagNames) {
        Set<String> usageMethodFlagNames = flagService.getAllFlagsOfMethod(usageMethod)
                .map(flag -> flag.value().isEmpty() ? flag.name() : flag.value())
                .collect(Collectors.toSet());

        SymmetricDifference<String> result =
                SetOperations.getSymmetricDifference(cliFlagNames, usageMethodFlagNames);

        if (!result.getSecondWithoutFirst().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.getFirstWithoutSecond());
    }

    private boolean allCliFlagsInMethodParams(Method usageMethod, Set<String> cliFlagNames) {
        Set<String> usageMethodBoolParams = flagService.getAllBooleanParametersOfMethod(usageMethod)
                .map(flagService::getFlagNameOfParameter)
                .collect(Collectors.toSet());

        return SetOperations.getSymmetricDifference(cliFlagNames, usageMethodBoolParams).getFirstWithoutSecond()
                .isEmpty();
    }

}
