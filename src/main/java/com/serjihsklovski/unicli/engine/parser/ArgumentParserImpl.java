package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.engine.lexer.Lexeme;
import com.serjihsklovski.unicli.engine.parser.exception.ArgumentParserException;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.UsageService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ArgumentParserImpl implements ArgumentParser {

    private enum StateItem {

        NONE,

        TASK_INIT,

    }

    private class ArgumentParserState {

        private StateItem stateItem;

        private List<ParameterizedTaskWrapper> taskQueue;

        ArgumentParserState() {
            stateItem = StateItem.NONE;
            taskQueue = new ArrayList<>();
        }

        List<ParameterizedTaskWrapper> getTaskQueue() {
            return taskQueue;
        }

        void addNewTaskToQueue(ParameterizedTaskWrapper newTask) {
            taskQueue.add(newTask);
        }

        ParameterizedTaskWrapper getLastTask() {
            return taskQueue.get(taskQueue.size() - 1);
        }

        ParameterizedTaskWrapper getLastButOneTask() {
            return taskQueue.get(taskQueue.size() - 2);
        }

        StateItem getStateItem() {
            return stateItem;
        }

        void setStateItem(StateItem stateItem) {
            this.stateItem = stateItem;
        }

    }

    private TaskService taskService;

    private UsageService usageService;

    public ArgumentParserImpl(TaskService taskService, UsageService usageService) {
        this.taskService = taskService;
        this.usageService = usageService;
    }

    @Override
    public List<ParameterizedTaskWrapper> parseLexemes(Stream<Lexeme> lexemeAssumptions) {
        ArgumentParserState state = new ArgumentParserState();
        lexemeAssumptions.forEach(lexeme -> {
            switch (lexeme.getType()) {
                case TASK_NAME_OR_VALUE:
                    switch (state.getStateItem()) {
                        case NONE: {
                            Class taskClass = getTaskClassByNameOrThrow(lexeme.getValue());
                            state.addNewTaskToQueue(new ParameterizedTaskWrapper(taskClass));
                            state.setStateItem(StateItem.TASK_INIT);
                            break;
                        }

                        case TASK_INIT: {
                            Class taskClass = getTaskClassByNameOrThrow(lexeme.getValue());
                            state.addNewTaskToQueue(new ParameterizedTaskWrapper(taskClass));

                            ParameterizedTaskWrapper lastButOneTask = state.getLastButOneTask();
                            // TODO: find the usage by parameters
                            Method lastActionUsage = usageService.getAllUsagesByTaskClass(lastButOneTask.getTaskClass())
                                    .findFirst()
                                    .orElseThrow(() -> new ArgumentParserException("Cannot find any usages."));
                            lastButOneTask.setUsage(lastActionUsage);

                            break;
                        }

                        default:
                            throw new ArgumentParserException(String.format("No actions supported for this state: " +
                                    "`lexeme.type` = %s, `state.stateItem` = %s", lexeme.getType(), state.stateItem));
                    }
                    break;

                default:
                    throw new ArgumentParserException(String.format("No actions supported for `%s` lexeme type state.",
                            lexeme.getType()));
            }
        });

        if (state.getTaskQueue().isEmpty()) {
            Class rootTask = getRootTaskOrThrow();
            ParameterizedTaskWrapper singleRootTask = new ParameterizedTaskWrapper(rootTask);
            // TODO: find the usages without any parameters
            Method usage = usageService.getAllUsagesByTaskClass(singleRootTask.getTaskClass())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot find any usages."));
            singleRootTask.setUsage(usage);
            state.addNewTaskToQueue(singleRootTask);
        } else if (state.getStateItem() == StateItem.TASK_INIT) {
            ParameterizedTaskWrapper lastTask = state.getLastTask();
            // TODO: find the usage by parameters
            Method lastActionUsage = usageService.getAllUsagesByTaskClass(lastTask.getTaskClass())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot find any usages."));
            lastTask.setUsage(lastActionUsage);
        }

        return state.getTaskQueue();
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
