package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public class UsageMethod {

    public static final long USAGES_COUNT = 2;

    @Usage
    public void usage1() {
    }

    @Usage
    public void usage2() {
    }

}
