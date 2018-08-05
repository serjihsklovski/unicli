package com.serjihsklovski.unicli.util.runner;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * A simple utility class to run sequences of instructions.
 */
public final class LazyActionsRunner {

    /**
     * Invokes {@link Supplier#get()} for each builder,
     * and then invokes {@link Runnable#run()} for each
     * runnable.
     *
     * @param lazyActionBuilders runnable suppliers
     */
    public static void run(Collection<? extends Supplier<Runnable>> lazyActionBuilders) {
        lazyActionBuilders.stream()
                .map(Supplier::get)
                .forEach(Runnable::run);
    }

}
