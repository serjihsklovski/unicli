package com.serjihsklovski.unicli.service.cli.test;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task("demo-task")
public class DemoTask {

    @Usage
    public void defaultUsage() {
    }

}
