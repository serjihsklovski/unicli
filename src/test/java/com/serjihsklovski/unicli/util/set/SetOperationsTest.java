package com.serjihsklovski.unicli.util.set;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class SetOperationsTest {

    @Test
    public void getSymmetricDifferenceTest() {
        SymmetricDifference<String> result = SetOperations.getSymmetricDifference(
                new HashSet<>(Arrays.asList("aaa", "bbb", "ccc", "ddd")),
                new HashSet<>(Arrays.asList("fff", "eee", "ddd", "ccc"))
        );

        assertEquals(new HashSet<>(Arrays.asList("aaa", "bbb")), result.getFirstWithoutSecond());
        assertEquals(new HashSet<>(Arrays.asList("fff", "eee")), result.getSecondWithoutFirst());
        assertEquals(new HashSet<>(Arrays.asList("aaa", "bbb", "eee", "fff")), result.getUnionResult());
    }

}
