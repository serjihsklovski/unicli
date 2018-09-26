package com.serjihsklovski.unicli.service;

import com.serjihsklovski.unicli.exception.NonTaskClassException;
import com.serjihsklovski.unicli.exception.UsageAnnotationMisuseException;
import com.serjihsklovski.unicli.service.test.usage.AbstractUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.FindUsageByFlagsTask;
import com.serjihsklovski.unicli.service.test.usage.FindUsageByParamsTask;
import com.serjihsklovski.unicli.service.test.usage.NonPublicUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.NonTaskClass;
import com.serjihsklovski.unicli.service.test.usage.StaticUsageMethod;
import com.serjihsklovski.unicli.service.test.usage.UsageMethod;
import com.serjihsklovski.unicli.service.test.usage.UsageMethodReturnsNonVoid;
import com.serjihsklovski.unicli.service.test.usage.UsageMethodTakesParams;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UsageServiceImplTest {

    private UsageService usageService;

    @Before
    public void setup() {
        usageService = new UsageServiceImpl(new FlagServiceImpl());
    }

    @Test
    public void getAllUsagesByTaskClass_expectOk() {
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

    @Test
    public void getAllUsagesByTaskClass_usageMethodTakesParams_expectOk() {
        assertEquals(
                String.format(
                        "There are %d usage methods in `%s` class.",
                        UsageMethodTakesParams.USAGES_COUNT,
                        UsageMethodTakesParams.class.getCanonicalName()
                ),
                usageService.getAllUsagesByTaskClass(UsageMethodTakesParams.class).count(),
                UsageMethodTakesParams.USAGES_COUNT
        );
    }

    @Test(expected = NonTaskClassException.class)
    public void getAllUsagesByTaskClass_expectNonClassTaskException() {
        usageService.getAllUsagesByTaskClass(NonTaskClass.class).forEach(method -> { /* no actions */ });
    }

    // TODO: enable abstract Usage methods in interfaces
    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskCLass_expectUsageAnnotationMisuseException_asAbstractUsageMethod() {
        usageService.getAllUsagesByTaskClass(AbstractUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asNonPublicUsageMethod() {
        usageService.getAllUsagesByTaskClass(NonPublicUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asStaticUsageMethod() {
        usageService.getAllUsagesByTaskClass(StaticUsageMethod.class).forEach(method -> { /* no actions */ });
    }

    @Test(expected = UsageAnnotationMisuseException.class)
    public void getAllUsagesByTaskClass_expectUsageAnnotationMisuseException_asUsageMethodReturnsNonVoid() {
        usageService.getAllUsagesByTaskClass(UsageMethodReturnsNonVoid.class).forEach(method -> { /* no actions */ });
    }

    @Test
    public void getUsageByTaskClassAndFlagNames_flagsAnnotation_expectOk() {
        List<Pair<Set<String>, String>> given = Arrays.asList(
                new Pair<>(new HashSet<>(), "usage"),
                new Pair<>(Stream.of("aaa").collect(Collectors.toSet()), "usage_aaa"),
                new Pair<>(Stream.of("aab").collect(Collectors.toSet()), "usage_aab"),
                new Pair<>(Stream.of("aac").collect(Collectors.toSet()), "usage_aac"),
                new Pair<>(Stream.of("aaa", "aab").collect(Collectors.toSet()), "usage_aaa_aab"),
                new Pair<>(Stream.of("aab", "aac").collect(Collectors.toSet()), "usage_aab_aac"),
                new Pair<>(Stream.of("aac", "aaa").collect(Collectors.toSet()), "usage_aac_aaa"),
                new Pair<>(Stream.of("aaa", "aab", "aac").collect(Collectors.toSet()), "usage_aaa_aab_aac")
        );

        for (Pair<Set<String>, String> pair : given) {
            testFlags(FindUsageByFlagsTask.class, pair.getKey(), pair.getValue());
        }
    }

    @Test
    public void getUsageByTaskClassAndFlagNames_boolParams_expectOk() {
        List<Pair<Set<String>, String>> given = Arrays.asList(
                new Pair<>(Stream.of("aaa").collect(Collectors.toSet()), "usage_aaa"),
                new Pair<>(new HashSet<>(), "usage_bbb_ccc_ddd"),
                new Pair<>(Stream.of("bbb").collect(Collectors.toSet()), "usage_bbb_ccc_ddd"),
                new Pair<>(Stream.of("bbb", "ccc").collect(Collectors.toSet()), "usage_bbb_ccc_ddd"),
                new Pair<>(Stream.of("ccc", "ddd").collect(Collectors.toSet()), "usage_bbb_ccc_ddd"),
                new Pair<>(Stream.of("ddd", "bbb").collect(Collectors.toSet()), "usage_bbb_ccc_ddd"),
                new Pair<>(Stream.of("bbb", "ccc", "ddd").collect(Collectors.toSet()), "usage_bbb_ccc_ddd")
        );

        for (Pair<Set<String>, String> pair : given) {
            testFlags(FindUsageByParamsTask.class, pair.getKey(), pair.getValue());
        }
    }

    private void testFlags(Class taskClass, Set<String> givenFlags, String expectedUsageName) {
        Optional<Method> actual = usageService.getUsageByTaskClassAndFlagNames(taskClass, givenFlags);
        assertTrue(actual.isPresent());
        assertEquals(expectedUsageName, actual.get().getName());
    }

}
