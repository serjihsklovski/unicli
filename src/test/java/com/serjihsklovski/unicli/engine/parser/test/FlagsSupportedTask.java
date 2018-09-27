package com.serjihsklovski.unicli.engine.parser.test;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task("flags-support")
public class FlagsSupportedTask {

    private static int usageId = 0;

    public static int getUsageId() {
        return usageId;
    }

    @Usage
    public static void usageWithoutFlags() {
        usageId = 1;
    }

    @Usage(flags = @Flag("aaa"))
    public static void usageWithFlagAaa() {
        usageId = 2;
    }

    @Usage(flags = @Flag("bbb"))
    public static void usageWithFlagBbb() {
        usageId = 3;
    }

    @Usage(flags = {@Flag("aaa"), @Flag("bbb")})
    public static void usageWithFlagsAaaBbb() {
        usageId = 4;
    }

}
