package com.serjihsklovski.unicli.util.set;

import java.util.Set;

/**
 * Common set operations, that are useful for Unicli development.
 */
public final class SetOperations {

    /**
     * Calculates elements which are in both of sets and not in their intersection.
     *
     * @param first the first set operand
     * @param second the second set operand
     * @param <T> the type of the set operation
     * @return an intermediate result
     */
    public static <T> SymmetricDifference<T> getSymmetricDifference(Set<T> first, Set<T> second) {
        SymmetricDifference<T> result = new SymmetricDifference<>();
        for (T val : first) {
            if (!second.contains(val)) {
                result.getFirstWithoutSecond().add(val);
            }
        }
        for (T val : second) {
            if (!first.contains(val)) {
                result.getSecondWithoutFirst().add(val);
            }
        }
        return result;
    }

}
