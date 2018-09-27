package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public class UsageMethodTakesParams {

    public static final long USAGES_COUNT = 1;

    @Usage
    public void usage(String msg) {
    }

}
