package com.serjihsklovski.unicli.engine.lexer;

import java.util.stream.Stream;

/**
 * Argument lexer makes assumptions about what type
 * of the JAR arguments are given into a program.
 * To do that, we use the following wrapper class:
 * @see com.serjihsklovski.unicli.engine.lexer.Lexeme
 *
 * In most cases, the argument lexer cannot accept
 * the exact type of the lexeme - this is a responsibility
 * of another processors (e.g. parsers, interpreters).
 * It also does not provide a stateful argument processing.
 *
 * You can find all the lexeme types in the following enumeration:
 * @see com.serjihsklovski.unicli.engine.lexer.Lexeme.LexemeType
 */
public interface ArgumentLexer {

    /**
     * Makes an assumption for each argument in the
     * given array of arguments.
     *
     * @param args "raw" arguments coming from a program
     * @return stream of lexemes
     * @see com.serjihsklovski.unicli.engine.lexer.Lexeme
     */
    Stream<Lexeme> getLexemes(String... args);

}
