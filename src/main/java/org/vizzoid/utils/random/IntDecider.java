package org.vizzoid.utils.random;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FunctionalInterface
public interface IntDecider {

    int next();

    static IntDecider always(int i) {
        return () -> i;
    }

    static IntDecider between(int i1, int i2) {
        return between(ThreadLocalRandom.current(), i1, i2);
    }

    static IntDecider between(Random r, int i1, int i2) {
        int trueI1 = Math.min(i1, i2);
        int trueI2 = Math.max(i1, i2);
        return () -> r.nextInt(trueI1, trueI2 + 1);
    }

    @SafeVarargs
    static IntDecider weighted(WeightedEntry<Integer>... entries) {
        return new PrivateClassContainer.Weighted(entries);
    }

    static IntDecider weighted(List<WeightedEntry<Integer>> entries) {
        return new PrivateClassContainer.Weighted(entries);
    }

    class PrivateClassContainer {

        private static class Weighted implements IntDecider {

            private final WeightedRandom<Integer> random;

            @SafeVarargs
            public Weighted(WeightedEntry<Integer>... entries) {
                this(List.of(entries));
            }

            public Weighted(List<WeightedEntry<Integer>> entries) {
                random = new WeightedRandom<>(entries);
            }

            @Override
            public int next() {
                return random.next();
            }
        }
    }

}
