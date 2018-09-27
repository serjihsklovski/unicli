package com.serjihsklovski.unicli.service;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * An interface to interact with the task class methods annotated with
 * the `@Usage` annotation.
 *
 * @see com.serjihsklovski.unicli.annotation.Usage
 */
public interface UsageService {

    /**
     * Looks for all Usages that are defined in the Unicli task class.
     *
     * @param taskClass Unicli task class
     * @return Usages for the Unicli task class
     * @see com.serjihsklovski.unicli.annotation.Usage
     */
    Stream<Method> getAllUsagesByTaskClass(Class taskClass);

    /**
     * Looks for the usage that matches all the flag names given, and
     * returns the single result.
     *
     * @param taskClass a Unicli task class, from the usage methods
     *                  of which the target usage will be searched
     * @param flagNames a set of flags that the target usage should match
     * @return the found usage
     */
    Optional<Method> getUsageByTaskClassAndFlagNames(Class taskClass, Set<String> flagNames);

}
