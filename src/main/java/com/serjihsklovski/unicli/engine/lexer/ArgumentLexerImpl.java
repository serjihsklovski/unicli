package com.serjihsklovski.unicli.engine.lexer;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ArgumentLexerImpl implements ArgumentLexer {

    @Override
    public Stream<Lexeme> getLexemes(String... args) {
        return Stream.of(args)
                .map(arg -> {
                    if (isValidTaskIdentifier(arg)) {
                        return new Lexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, arg);
                    } else if (isOption(arg)) {
                        return new Lexeme(Lexeme.LexemeType.OPTION_OR_VALUE, arg);
                    } else if (isParameter(arg)) {
                        return new Lexeme(Lexeme.LexemeType.PARAMETER_OR_VALUE, arg);
                    } else if (isShortOptionParamList(arg)) {
                        return new Lexeme(Lexeme.LexemeType.SHORTCUTS_OR_VALUE, arg);
                    } else {
                        return new Lexeme(Lexeme.LexemeType.VALUE, arg);
                    }
                });
    }

    private boolean isValidTaskIdentifier(String arg) {
        return Pattern.compile("^[a-zA-Z_](?:-?[a-zA-Z_\\d]+)*$").matcher(arg).matches();
    }

    private boolean isOption(String arg) {
        return Pattern.compile("^--[a-zA-Z_](?:-?[a-zA-Z_\\d]+)*$").matcher(arg).matches();
    }

    private boolean isParameter(String arg) {
        return Pattern.compile("^--[a-zA-Z_](?:-?[a-zA-Z_\\d]+)*=.*$").matcher(arg).matches();
    }

    private boolean isShortOptionParamList(String arg) {
        return Pattern.compile("^-[a-zA-Z]+$").matcher(arg).matches();
    }

}
