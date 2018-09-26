package com.serjihsklovski.unicli;

import com.serjihsklovski.unicli.engine.lexer.ArgumentLexer;
import com.serjihsklovski.unicli.engine.lexer.ArgumentLexerImpl;
import com.serjihsklovski.unicli.engine.parser.ArgumentParser;
import com.serjihsklovski.unicli.engine.parser.ArgumentParserImpl;
import com.serjihsklovski.unicli.service.FlagService;
import com.serjihsklovski.unicli.service.FlagServiceImpl;
import com.serjihsklovski.unicli.service.TaskService;
import com.serjihsklovski.unicli.service.TaskServiceImpl;
import com.serjihsklovski.unicli.service.UsageService;
import com.serjihsklovski.unicli.service.UsageServiceImpl;
import com.serjihsklovski.unicli.util.classprovider.ClassProvider;
import com.serjihsklovski.unicli.util.classprovider.ClassProviderImpl;
import com.serjihsklovski.unicli.util.runner.LazyActionsRunner;

import java.util.Collections;
import java.util.Set;

public class Unicli {

    public static void run(String root, String... args) {
        ClassProvider classProvider = new ClassProviderImpl();
        Set<Class> classPool = classProvider.fetchAllClassesByRoots(Collections.singleton(root));

        TaskService taskService = new TaskServiceImpl(classPool);
        FlagService flagService = new FlagServiceImpl();
        UsageService usageService = new UsageServiceImpl(flagService);

        ArgumentLexer lexer = new ArgumentLexerImpl();
        ArgumentParser parser = new ArgumentParserImpl(taskService, usageService);

        LazyActionsRunner.run(parser.parseLexemes(lexer.getLexemes(args)));
    }

}
