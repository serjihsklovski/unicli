package com.serjihsklovski.unicli.service;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public interface UsageService {

    /**
     * Looks for all Usages that are defined in the Unicli task class.
     *
     * @param taskClass Unicli task class
     * @return Usages for the Unicli task class
     * @see com.serjihsklovski.unicli.annotation.Usage
     */
    Stream<Method> getAllUsagesByTaskClass(Class taskClass);

}
