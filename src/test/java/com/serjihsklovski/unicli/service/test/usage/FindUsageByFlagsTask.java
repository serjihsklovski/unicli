package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public class FindUsageByFlagsTask {

    @Usage
    public void usage() {
    }

    @Usage(flags = {@Flag("aaa")})
    public void usage_aaa() {
    }

    @Usage(flags = {@Flag("aab")})
    public void usage_aab() {
    }

    @Usage(flags = {@Flag("aac")})
    public void usage_aac() {
    }

    @Usage(flags = {@Flag("aaa"), @Flag("aab")})
    public void usage_aaa_aab() {
    }

    @Usage(flags = {@Flag("aab"), @Flag("aac")})
    public void usage_aab_aac() {
    }

    @Usage(flags = {@Flag("aac"), @Flag("aaa")})
    public void usage_aac_aaa() {
    }

    @Usage(flags = {@Flag("aaa"), @Flag("aab"), @Flag("aac")})
    public void usage_aaa_aab_aac() {
    }

}
