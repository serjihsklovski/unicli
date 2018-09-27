package com.serjihsklovski.unicli.util.set;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The intermediate result of the symmetric difference set operation.
 * To get the final result, use the following methods:
 * - `getFirstWithoutSecond`;
 * - `getSecondWithoutFirst`;
 * - `getUnionResult`
 *
 * @param <T> the type of set entries
 * @see SetOperations#getSymmetricDifference(Set, Set)
 */
public final class SymmetricDifference<T> {

    private final Set<T> firstWithoutSecond;

    private final Set<T> secondWithoutFirst;

    public SymmetricDifference() {
        this.firstWithoutSecond = new HashSet<>();
        this.secondWithoutFirst = new HashSet<>();
    }

    /**
     * @return A - B
     */
    public Set<T> getFirstWithoutSecond() {
        return firstWithoutSecond;
    }

    /**
     * @return B - A
     */
    public Set<T> getSecondWithoutFirst() {
        return secondWithoutFirst;
    }

    /**
     * @return (A - B) + (B - A)
     */
    public Set<T> getUnionResult() {
        return Stream.concat(getFirstWithoutSecond().stream(), getSecondWithoutFirst().stream())
                .collect(Collectors.toSet());
    }

}
