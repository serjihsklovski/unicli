package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.exception.TaskAnnotationMisuseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void getTaskByName_expectOk() {
        TaskService taskService = initWithClassPool(com.serjihsklovski.unicli.service.cli.test.DemoTask.class);

        Optional<Class> rootTaskClass = taskService.getTaskByName("demo-task");
        assertTrue(rootTaskClass.isPresent());
    }

    @Test
    public void getTaskByName_expectNotFound() {
        TaskService taskService = initWithClassPool(com.serjihsklovski.unicli.service.cli.test.DemoTask.class);

        Optional<Class> rootTaskClass = taskService.getTaskByName("nonexistent-task");
        assertFalse(rootTaskClass.isPresent());
    }

    @Test
    public void getRootTask_expectOk() {
        TaskService taskService = initWithClassPool(
                com.serjihsklovski.unicli.service.cli.test.DemoRootTask.class,
                com.serjihsklovski.unicli.service.cli.test.DemoTask.class
        );

        Optional<Class> rootTaskClass = taskService.getRootTask();
        assertTrue(rootTaskClass.isPresent());
    }

    @Test
    public void getRootTask_expectNotFound() {
        TaskService taskService = initWithClassPool(com.serjihsklovski.unicli.service.cli.test.DemoTask.class);

        Optional<Class> rootTaskClass = taskService.getRootTask();
        assertFalse(rootTaskClass.isPresent());
    }

    private TaskServiceImpl initWithClassPool(Class... classes) {
        return new TaskServiceImpl(Stream.of(classes).collect(Collectors.toSet()));
    }

    private void testClassPool(Set<Class> classPool) {
        TaskService taskService = new TaskServiceImpl(classPool);
        taskService.getAllTaskClasses().forEach(clazz -> { /* no action */ });
    }

    @Task(name = "test-task")
    private class TestTask1 {
    }

    @Test
    public void getTaskNameByClass_nameProperty_expectOk() {
        TaskService taskService = new TaskServiceImpl(new HashSet<>());
        assertEquals("test-task", taskService.getTaskNameByClass(TestTask1.class)
                .orElseThrow(() -> new RuntimeException("Cannot find task name by class.")));
    }

    @Task("test-task")
    private class TestTask2 {
    }

    @Test
    public void getTaskNameByClass_valueProperty_expectOk() {
        TaskService taskService = new TaskServiceImpl(new HashSet<>());
        assertEquals("test-task", taskService.getTaskNameByClass(TestTask2.class)
                .orElseThrow(() -> new RuntimeException("Cannot find task name by class.")));
    }

    @Task(root = true)
    private class TestTask3 {
    }

    @Test
    public void getTaskNameByClass_noNameAsRoot_expectEmpty() {
        TaskService taskService = new TaskServiceImpl(new HashSet<>());
        assertFalse(taskService.getTaskNameByClass(TestTask3.class).isPresent());
    }

    @Task
    private class TestTask4 {
    }

    @Test
    public void getTaskNameByClass_noName_expectThrown() {
        TaskService taskService = new TaskServiceImpl(new HashSet<>());
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format("Unicli task class `%s` has no name declared.",
                TestTask4.class.getCanonicalName()));
        taskService.getTaskNameByClass(TestTask4.class);
    }

    private class TestTask5 {
    }

    @Test
    public void getTaskNameByClass_noTask_expectThrown() {
        TaskService taskService = new TaskServiceImpl(new HashSet<>());
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(String.format(
                "Class `%s` is not the Unicli task class (it is not annotated with `%s` annotation).",
                TestTask5.class.getCanonicalName(), Task.class.getCanonicalName()));
        taskService.getTaskNameByClass(TestTask5.class);
    }

}
