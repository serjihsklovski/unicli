package com.serjihsklovski.unicli.service.test.usage;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task
public class FindUsageByParamsTask {

    @Usage(flags = {
            @Flag("aaa")
    })
    public void usage_aaa() {
    }

    @Usage
    public void usage_bbb_ccc_ddd(@Flag("bbb") boolean bbb, @Flag("ccc") boolean xxx, @Flag(name = "ddd") Boolean yyy) {
    }

}
