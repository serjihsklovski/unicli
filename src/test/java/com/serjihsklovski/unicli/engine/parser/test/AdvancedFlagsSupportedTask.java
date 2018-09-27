package com.serjihsklovski.unicli.engine.parser.test;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

import java.util.ArrayList;
import java.util.List;

@Task("advanced-flags-support")
public class AdvancedFlagsSupportedTask {

    private static List<Integer> usageInvocationOrder = new ArrayList<>();

    public static List<Integer> getUsageInvocationOrder() {
        return usageInvocationOrder;
    }

    @Usage
    public static void usageWithoutFlags() {
        usageInvocationOrder.add(1);
    }

    @Usage(flags = @Flag("aaa"))
    public static void usageWithFlagAaa() {
        usageInvocationOrder.add(2);
    }

    @Usage(flags = @Flag("bbb"))
    public static void usageWithFlagBbb() {
        usageInvocationOrder.add(3);
    }

    @Usage(flags = {@Flag("aaa"), @Flag("bbb")})
    public static void usageWithFlagsAaaBbb() {
        usageInvocationOrder.add(4);
    }

}
