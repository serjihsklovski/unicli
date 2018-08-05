package com.serjihsklovski.unicli.service.cli.test;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task(root = true)
public class DemoRootTask {

    @Usage
    public void defaultUsage() {
    }

}
