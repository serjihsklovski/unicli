package com.serjihsklovski.unicli.test.unicli;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task(root = true)
public class DemoApplication {

    public static final int EXPECTED_VALUE = 1;

    private static int value;

    @Usage
    public void setValue() {
        value = EXPECTED_VALUE;
    }

    public static int getValue() {
        return value;
    }

}
