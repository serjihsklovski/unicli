package com.serjihsklovski.unicli;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;
import com.serjihsklovski.unicli.exception.AmbiguityException;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.TaskServiceImpl;
import com.serjihsklovski.unicli.service.UsageService;
import com.serjihsklovski.unicli.service.UsageServiceImpl;
import com.serjihsklovski.unicli.util.classprovider.ClassProvider;
import com.serjihsklovski.unicli.util.classprovider.ClassProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Unicli {

    // TODO: temporary implementation to enable single Task with its single Usage (v0.1.0)
    public static void run(String root, String... args) {
        if (args.length > 0) {
            throw new RuntimeException("Cannot parse arguments now :(");
        }
        ClassProvider classProvider = new ClassProviderImpl();
        Set<Class> classPool = classProvider.fetchAllClassesByRoots(Collections.singleton(root));

        TaskService taskService = new TaskServiceImpl(classPool);
        Set<Class> taskClasses = taskService.getAllTaskClasses().collect(Collectors.toSet());
        if (taskClasses.size() == 0) {
            return;
        }
        if (taskClasses.size() > 1) {
            throw new AmbiguityException(String.format("Ambiguity: there are multiple classes annotated with `%s`.",
                    Task.class.getCanonicalName()));
        }
        Class<?> taskClass = taskClasses.stream().findFirst().orElseThrow(RuntimeException::new);

        UsageService usageService = new UsageServiceImpl();
        Set<Method> usageMethods = usageService.getAllUsagesByTaskClass(taskClass).collect(Collectors.toSet());
        if (usageMethods.size() == 0) {
            return;
        }
        if (usageMethods.size() > 1) {
            throw new AmbiguityException(String.format("Ambiguity: there are multiple methods annotated with `%s`.",
                    Usage.class.getCanonicalName()));
        }
        Method usageMethod = usageMethods.stream().findFirst().orElseThrow(RuntimeException::new);

        try {
            Object object = taskClass.newInstance();
            usageMethod.invoke(object);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
