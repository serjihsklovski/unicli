package com.serjihsklovski.unicli;

import com.serjihsklovski.unicli.test.unicli.DemoApplication;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnicliTest {

    @Test
    public void run_expectOk() {
        Unicli.run("com.serjihsklovski.unicli.test.unicli");
        assertEquals(
                String.format("Unicli demo application should set value to %d.", DemoApplication.EXPECTED_VALUE),
                DemoApplication.EXPECTED_VALUE,
                DemoApplication.getValue()
        );
    }

}
