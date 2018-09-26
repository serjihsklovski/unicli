package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.engine.lexer.Lexeme;
import com.serjihsklovski.unicli.engine.parser.exception.ArgumentParserException;
import com.serjihsklovski.unicli.engine.parser.test.AnotherDemoTask;
import com.serjihsklovski.unicli.engine.parser.test.DemoRootTask;
import com.serjihsklovski.unicli.engine.parser.test.DemoTask;
import com.serjihsklovski.unicli.service.FlagServiceImpl;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.UsageService;
import com.serjihsklovski.unicli.service.UsageServiceImpl;
import com.serjihsklovski.unicli.util.runner.LazyActionsRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

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
                AnotherDemoTask.class));
        when(taskService.getRootTask()).thenReturn(Optional.of(DemoRootTask.class));
        when(taskService.getTaskByName("demo-task")).thenReturn(Optional.of(DemoTask.class));
        when(taskService.getTaskByName("another-demo-task")).thenReturn(Optional.of(AnotherDemoTask.class));

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

}
