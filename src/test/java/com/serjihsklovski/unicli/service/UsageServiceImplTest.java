package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.exception.NonTaskClassException;
import com.serjihsklovski.unicli.exception.UsageAnnotationMisuseException;
import com.serjihsklovski.unicli.exception.UsageMethodTakesParamsException;
import com.serjihsklovski.unicli.service.test.usage.AbstractUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.NonPublicUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.NonTaskClass;
import com.serjihsklovski.unicli.service.test.usage.StaticUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.UsageMethod;
import com.serjihsklovski.unicli.service.test.usage.UsageMethodReturnsNonVoid;
import com.serjihsklovski.unicli.service.test.usage.UsageMethodTakesParams;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UsageServiceImplTest {

    @Test
    public void getAllUsagesByTaskClass_expectOk() {
        UsageService usageService = new UsageServiceImpl();
        assertEquals(
                String.format(
                        "There are %d Usage methods in `%s` class.",
                        UsageMethod.USAGES_COUNT,
                        UsageMethod.class.getCanonicalName()
                ),
                usageService.getAllUsagesByTaskClass(UsageMethod.class).count(),
                UsageMethod.USAGES_COUNT
        );
    }

    @Test(expected = NonTaskClassException.class)
    public void getAllUsagesByTaskClass_expectNonClassTaskException() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(NonTaskClass.class).forEach(method -> { /* no actions */ });
    }

    // TODO: enable abstract Usage methods in interfaces
    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskCLass_expectUsageAnnotationMisuseException_asAbstractUsageMethod() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(AbstractUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asNonPublicUsageMethod() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(NonPublicUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asStaticUsageMethod() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(StaticUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asUsageMethodReturnsNonVoid() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(UsageMethodReturnsNonVoid.class).forEach(method -> { /* no actions */ });
    }

    // TODO: enable parametrized Usage methods
    @Test(expected = UsageMethodTakesParamsException.class)
    public void getAllUsagesByTaskClass_expectUsageMethodTakesParamsException() {
        UsageService usageService = new UsageServiceImpl();
        usageService.getAllUsagesByTaskClass(UsageMethodTakesParams.class).forEach(method -> { /* no actions */ });
    }

}
