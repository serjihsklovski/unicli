package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.exception.TaskAnnotationMisuseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TaskServiceImplTest {

    @Test
    public void getAllTaskClasses_expectOk() {
        testClassPool(new HashSet<>(Arrays.asList(
                com.serjihsklovski.unicli.service.test.TaskClass.class//, TODO: enable @Task for interfaces
//                com.serjihsklovski.unicli.service.test.TaskInterface.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseAbstractClass() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.TaskAbstractClass.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseAnnotation() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.TaskAnnotation.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseEnum() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.TaskEnum.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseNonPublicClass() {
        try {
            testClassPool(new HashSet<>(Collections.singletonList(
                    // cannot access package-private classes as-is, so we have to call this:
                    Class.forName("com.serjihsklovski.unicli.service.test.TaskNonPublicClass")
            )));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void testClassPool(Set<Class> classPool) {
        TaskService taskService = new TaskServiceImpl(classPool);
        taskService.getAllTaskClasses().forEach(clazz -> { /* no action */ });
    }

}
