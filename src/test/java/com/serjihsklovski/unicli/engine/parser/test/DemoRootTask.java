package com.serjihsklovski.unicli.engine.parser.test;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task(root = true)
public class DemoRootTask {

    private static int runsCount = 0;

    @Usage
    public void defaultUsage() {
        runsCount++;
    }

    public static int getRunsCount() {
        return runsCount;
    }

    public static void setRunsCount(int runsCount) {
        DemoRootTask.runsCount = runsCount;
    }

}
