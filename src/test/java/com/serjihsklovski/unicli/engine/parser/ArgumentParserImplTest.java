package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.engine.lexer.Lexeme;
import com.serjihsklovski.unicli.engine.parser.exception.ArgumentParserException;
import com.serjihsklovski.unicli.engine.parser.test.*;
import com.serjihsklovski.unicli.service.FlagServiceImpl;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.UsageService;
import com.serjihsklovski.unicli.service.UsageServiceImpl;
import com.serjihsklovski.unicli.util.runner.LazyActionsRunner;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ArgumentParserImplTest {

    private ArgumentParser parser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        TaskService taskService = Mockito.mock(TaskService.class);
        when(taskService.getAllTaskClasses()).thenReturn(Stream.of(DemoRootTask.class, DemoTask.class,
                AnotherDemoTask.class, FlagsSupportedTask.class, AdvancedFlagsSupportedTask.class));
        when(taskService.getRootTask()).thenReturn(Optional.of(DemoRootTask.class));
        when(taskService.getTaskByName("demo-task")).thenReturn(Optional.of(DemoTask.class));
        when(taskService.getTaskByName("another-demo-task")).thenReturn(Optional.of(AnotherDemoTask.class));
        when(taskService.getTaskByName("flags-support")).thenReturn(Optional.of(FlagsSupportedTask.class));
        when(taskService.getTaskByName("advanced-flags-support")).thenReturn(Optional.of(AdvancedFlagsSupportedTask.class));

        UsageService usageService = new UsageServiceImpl(new FlagServiceImpl());

        parser = new ArgumentParserImpl(taskService, usageService);

        DemoRootTask.setRunsCount(0);
        DemoTask.setRunsCount(0);
        AnotherDemoTask.setRunsCount(0);
    }

    @Test
    public void parseLexemes_rootTask() {
        List<? extends Supplier<Runnable>> instructions = parser.parseLexemes(Stream.empty());
        assertEquals(1, instructions.size());

        LazyActionsRunner.run(instructions);
        assertEquals(1, DemoRootTask.getRunsCount());
    }

    @Test
    public void parseLexemes_namedTask() {
        Stream<Lexeme> lexemes = Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "demo-task"));
        List<? extends Supplier<Runnable>> instructions = parser.parseLexemes(lexemes);
        assertEquals(1, instructions.size());

        LazyActionsRunner.run(instructions);
        assertEquals(1, DemoTask.getRunsCount());
    }

    @Test
    public void parseLexemes_multipleTasks() {
        Stream<Lexeme> lexemes = Stream.of("demo-task", "another-demo-task")
                .map(taskName -> new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, taskName));
        List<? extends Supplier<Runnable>> instructions = parser.parseLexemes(lexemes);
        assertEquals(2, instructions.size());

        LazyActionsRunner.run(instructions);
        assertEquals(1, DemoTask.getRunsCount());
        assertEquals(1, AnotherDemoTask.getRunsCount());
    }

    @Test
    public void parseLexemes_nonexistentTask() {
        Stream<Lexeme> lexemes = Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "nonexistent-task"));
        thrown.expect(ArgumentParserException.class);
        thrown.expectMessage("No `nonexistent-task` task defined.");
        parser.parseLexemes(lexemes);
    }

    @Test
    public void parseLexemes_flagsSupport_singleTask() {
        Stream.of(
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa")), 4),
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "flags-support")), 1),
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa")), 2),
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb")), 3),
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb")), 4)
        ).forEach(testSet -> testSingleTaskWithFlags(testSet.getKey(), testSet.getValue()));
    }

    private void testSingleTaskWithFlags(Stream<Lexeme> lexemes, int expectedValue) {
        List<? extends Supplier<Runnable>> instructions = parser.parseLexemes(lexemes);
        assertEquals(1, instructions.size());
        LazyActionsRunner.run(instructions);
        assertEquals(expectedValue, FlagsSupportedTask.getUsageId());
    }

    @Test
    public void parseLexemes_advancedFlagsSupport_multipleTasks() {
        Stream.of(
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa"),
                        new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb")), Arrays.asList(2, 4)),
                new Pair<>(Stream.of(new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa"),
                        new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--aaa"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb"),
                        new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support"),
                        new Lexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--bbb"),
                        new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "advanced-flags-support")), Arrays.asList(2, 4, 3, 1))
        ).forEach(testSet -> testMultipleTasksWithFlags(testSet.getKey(), testSet.getValue()));
    }

    private void testMultipleTasksWithFlags(Stream<Lexeme> lexemes, List<Integer> tasksOrder) {
        List<? extends Supplier<Runnable>> instructions = parser.parseLexemes(lexemes);
        assertEquals(tasksOrder.size(), instructions.size());

        AdvancedFlagsSupportedTask.getUsageInvocationOrder().clear();
        LazyActionsRunner.run(instructions);
        assertEquals(tasksOrder, AdvancedFlagsSupportedTask.getUsageInvocationOrder());
    }

}
