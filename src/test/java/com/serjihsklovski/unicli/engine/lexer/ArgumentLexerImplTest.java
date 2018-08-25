package com.serjihsklovski.unicli.engine.lexer;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ArgumentLexerImplTest {

    private ArgumentLexer lexer;

    @Before
    public void setLexer() {
        lexer = new ArgumentLexerImpl();
    }

    @Test
    public void getLexemesTestPossibleTaskName() {
        getLexemesTestSingleLexeme(Lexeme.LexemeType.TASK_NAME_OR_VALUE, "install");
    }

    @Test
    public void getLexemesTestPossibleFlag() {
        getLexemesTestSingleLexeme(Lexeme.LexemeType.FLAG_OR_VALUE, "--version");
    }

    @Test
    public void getLexemesTestPossibleParameter() {
        getLexemesTestSingleLexeme(Lexeme.LexemeType.PARAMETER_OR_VALUE, "--file=/tmp/test");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.PARAMETER_OR_VALUE, "--no-value=");
    }

    @Test
    public void getLexemesTestPossibleShortcuts() {
        getLexemesTestSingleLexeme(Lexeme.LexemeType.SHORTCUTS_OR_VALUE, "-V");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.SHORTCUTS_OR_VALUE, "-xcf");
    }

    @Test
    public void getLexemesTestValues() {
        getLexemesTestSingleLexeme(Lexeme.LexemeType.VALUE, "");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.VALUE, "This is a test");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.VALUE, "123");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.VALUE, "-123");
        getLexemesTestSingleLexeme(Lexeme.LexemeType.VALUE, "--value-");
    }

    private void getLexemesTestSingleLexeme(Lexeme.LexemeType expectedLexemeType, String expectedLexemeValue) {
        List<Lexeme> lexemes = lexer.getLexemes(expectedLexemeValue).collect(Collectors.toList());
        assertThat(lexemes.size(), is(1));
        assertThat(lexemes.get(0).getType(), is(expectedLexemeType));
        assertThat(lexemes.get(0).getValue(), is(expectedLexemeValue));
    }

}
