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
                com.serjihsklovski.unicli.service.test.task.TaskClass.class//, TODO: enable @Task for interfaces
//                com.serjihsklovski.unicli.service.test.task.TaskInterface.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseAbstractClass() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.task.TaskAbstractClass.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseAnnotation() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.task.TaskAnnotation.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseEnum() {
        testClassPool(new HashSet<>(Collections.singletonList(
                com.serjihsklovski.unicli.service.test.task.TaskEnum.class
        )));
    }

    @Test(expected = TaskAnnotationMisuseException.class)
    public void getAllTaskClasses_expectTaskAnnotationMisuseException_becauseNonPublicClass() {
        try {
            testClassPool(new HashSet<>(Collections.singletonList(
                    // cannot access package-private classes as-is, so we have to call this:
                    Class.forName("com.serjihsklovski.unicli.service.test.task.TaskNonPublicClass")
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
