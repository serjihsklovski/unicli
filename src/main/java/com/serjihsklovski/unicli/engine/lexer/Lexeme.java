package com.serjihsklovski.unicli.engine.lexer;

/**
 * Represents an argument as a lexeme: `type` + `value`.
 */
public class Lexeme {

    /**
     * If the argument matches some pattern,
     * the lexer can assign the type to it.
     *
     * @see com.serjihsklovski.unicli.engine.lexer.ArgumentLexer
     */
    public enum LexemeType {

        /**
         * Represents strings, numbers, paths, etc.
         * The lexer can assign this type to an argument
         * if it does not match any pattern.
         */
        VALUE,

        /**
         * Represents task names. Examples:
         * `install`
         * `httpGet`
         * `my-task`
         */
        TASK_NAME_OR_VALUE,

        /**
         * Represents flags. Followed by a task, the flags
         * are related to this exact task, configuring its
         * behaviour. Flags start with 2 dashes. Examples:
         * `--verbose`
         * `--sync`
         */
        FLAG_OR_VALUE,

        /**
         * Represents parameters. Followed by a task, the parameters
         * are related to this exact task, setting its required and
         * optional parameters. Parameter lexeme consists of the parameter
         * name, assignment character, and argument value. Parameter names
         * start with 2 dashes. Examples:
         * `--file=/tmp/test`
         * `--level=4`
         * `--empty-value=`
         */
        PARAMETER_OR_VALUE,

        /**
         * Represents shortcuts. Usually, a shortcut is a one-character
         * synonym for a task flag/parameter. Less often, a shortcut
         * can be self-sufficient, i.e. there may not be a full flag or
         * parameter name at all. Many shortcuts can be joined into one
         * so-called shortcut list. The shortcut/shortcut list starts
         * with a dash. Examples:
         * `-o`
         * `-xcf`
         */
        SHORTCUTS_OR_VALUE,

    }

    private final LexemeType type;

    private final String value;

    public Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    public LexemeType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
