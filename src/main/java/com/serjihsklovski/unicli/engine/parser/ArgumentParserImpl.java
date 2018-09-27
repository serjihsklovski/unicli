package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.engine.lexer.Lexeme;
import com.serjihsklovski.unicli.engine.parser.exception.ArgumentParserException;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.UsageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ArgumentParserImpl implements ArgumentParser {

    private final TaskService taskService;

    private final UsageService usageService;

    public ArgumentParserImpl(TaskService taskService, UsageService usageService) {
        this.taskService = taskService;
        this.usageService = usageService;
    }

    @Override
    public List<ParameterizedTaskWrapper> parseLexemes(Stream<Lexeme> lexemeAssumptions) {
        List<ParameterizedTaskWrapper> taskQueue = new ArrayList<>();
        lexemeAssumptions.forEach(lexeme -> parseLexeme(taskQueue, lexeme));

        if (taskQueue.isEmpty()) {
            // no lexemes means that the root task will be invoked
            Class rootTask = getRootTaskOrThrow();
            ParameterizedTaskWrapper singleRootTask = new ParameterizedTaskWrapper(taskService, usageService, rootTask);
            taskQueue.add(singleRootTask);
        }

        return taskQueue;
    }

    private void parseLexeme(List<ParameterizedTaskWrapper> taskQueue, Lexeme lexeme) {
        switch (lexeme.getType()) {
            case TASK_NAME_OR_VALUE:
                Class taskClass = getTaskClassByNameOrThrow(lexeme.getValue());
                taskQueue.add(new ParameterizedTaskWrapper(taskService, usageService, taskClass));
                break;

            case FLAG_OR_VALUE:
                parseFlagOrValue(taskQueue, lexeme);
                break;

            default:
                throw new ArgumentParserException(String.format("No actions supported for `%s` lexeme type state.",
                        lexeme.getType()));
        }
    }

    private void parseFlagOrValue(List<ParameterizedTaskWrapper> taskQueue, Lexeme lexeme) {
        if (taskQueue.isEmpty()) {
            ParameterizedTaskWrapper newRootTask = new ParameterizedTaskWrapper(taskService, usageService, getRootTaskOrThrow());
            newRootTask.addRawFlag(lexeme.getValue());
            taskQueue.add(newRootTask);
        } else {
            taskQueue.get(taskQueue.size() - 1).addRawFlag(lexeme.getValue());
        }
    }

    private Class getTaskClassByNameOrThrow(String taskName) {
        Optional<Class> optionalTask = taskService.getTaskByName(taskName);
        if (!optionalTask.isPresent()) {
            throw new ArgumentParserException(String.format("No `%s` task defined.", taskName));
        }
        return optionalTask.get();
    }

    private Class getRootTaskOrThrow() {
        Optional<Class> optionalRootTask = taskService.getRootTask();
        if (!optionalRootTask.isPresent()) {
            throw new ArgumentParserException("No root task defined.");
        }
        return optionalRootTask.get();
    }

}
