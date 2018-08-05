package com.serjihsklovski.unicli.engine.parser;

import com.serjihsklovski.unicli.engine.lexer.Lexeme;
import com.serjihsklovski.unicli.engine.parser.exception.ArgumentParserException;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The argument parser's purpose is to create a sequence
 * of instructions that could be evaluated afterwards.
 */
public interface ArgumentParser {

    /**
     * Prepares a sequence of instructions relying on
     * assumptions made by the lexer. Then, the instructions
     * may be evaluated one by one.
     *
     * @param lexemeAssumptions from the argument lexer
     * @return a queue of actions
     * @see Lexeme
     * @throws ArgumentParserException when the parser cannot find a task, or a usage, etc.
     */
    List<? extends Supplier<Runnable>> parseLexemes(Stream<Lexeme> lexemeAssumptions);

}
