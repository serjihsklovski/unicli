package com.serjihsklovski.unicli;

import java.util.Set;

public interface ClassProvider {

    /**
     * Looks for all the classes that are in the packages provided by the `roots`.
     * The nested and inner classes are not included.
     *
     * @param roots One or many package names
     * @return All classes in the packages
     */
    Set<Class> fetchAllClassesByRoots(Set<String> roots);

}
