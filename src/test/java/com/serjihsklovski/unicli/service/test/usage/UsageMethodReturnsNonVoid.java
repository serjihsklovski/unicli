package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public class UsageMethodReturnsNonVoid {

    @Usage
    public int usage() {
        return 0;
    }

}
