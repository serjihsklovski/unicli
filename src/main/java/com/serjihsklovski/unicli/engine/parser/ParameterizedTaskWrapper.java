package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.service.FlagService;
import com.serjihsklovski.unicli.service.FlagServiceImpl;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.UsageService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class ParameterizedTaskWrapper implements Supplier<Runnable> {

    private final TaskService taskService;

    private final UsageService usageService;

    private final Class taskClass;

    private Set<String> flagNames;

    ParameterizedTaskWrapper(TaskService taskService, UsageService usageService, Class taskClass) {
        this.taskService = taskService;
        this.usageService = usageService;
        this.taskClass = taskClass;
        flagNames = new HashSet<>();
    }

    void addRawFlag(String rawFlag) {
        flagNames.add(rawFlag.substring(2));    // flag name separation from two forward dashes
    }

    @Override
    public Runnable get() {
        Method usage = usageService.getUsageByTaskClassAndFlagNames(taskClass, flagNames)
                .orElseThrow(() -> new RuntimeException(String.format(
                        "Cannot find an appropriate usage method for task `%s` (task class: `%s`, flags: [%s]).",
                        taskService.getTaskNameByClass(taskClass).orElse("default"), taskClass.getCanonicalName(),
                        String.join(", ", flagNames))));

        return () -> {
            try {
                FlagService flagService = new FlagServiceImpl();
                Object[] arguments = flagService.getAllBooleanParametersOfMethod(usage)
                        .map(param -> flagNames.contains(flagService.getFlagNameOfParameter(param)))
                        .toArray();

                // NOTE: `getTaskInstance` returns `Optional.empty()` if the usage is static, so the `null` is correct
                usage.invoke(getTaskInstance(usage.getModifiers()).orElse(null), arguments);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(String.format("Cannot invoke the method `%s`: access denied.",
                        usage.getName()), iae);
            } catch (InvocationTargetException ite) {
                throw new RuntimeException(ite);
            }
        };
    }

    private Optional<Object> getTaskInstance(int usageMethodModifiers) {
        if (!Modifier.isStatic(usageMethodModifiers)) {
            try {
                return Optional.of(taskClass.newInstance());
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(String.format("Cannot create an instance of the task class `%s`: access denied.",
                        taskClass.getCanonicalName()), iae);
            } catch (InstantiationException ie) {
                throw new RuntimeException(String.format("Cannot create an instance of a task class `%s`.",
                        taskClass.getCanonicalName()), ie);
            }
        }

        return Optional.empty();
    }

}
