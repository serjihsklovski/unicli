package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public abstract class AbstractUsageMethod {

    @Usage
    public abstract void usage();

}
