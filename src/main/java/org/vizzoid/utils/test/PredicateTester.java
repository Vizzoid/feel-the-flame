package org.vizzoid.utils.test;

import java.util.ArrayList;
import java.util.List;

public class PredicateTester<T> {

    private static class PredicateCase<T> {
        public final String key;
        public final TestPredicate<T> predicate;

        public PredicateCase(String key, TestPredicate<T> predicate) {
            this.key = key;
            this.predicate = predicate;
        }

        public void test(T t, List<String> failures) {
            if (predicate.test(t)) return;
            failures.add(key);
        }
    }
    private static class EqualsPredicateCase<T, T0> extends PredicateCase<T> {
        private final TestFunction<T, T0> mapper;
        private final T0 predicted;

        public EqualsPredicateCase(String key, TestFunction<T, T0> mapper, T0 predicted) {
            super(key, null);
            this.mapper = mapper;
            this.predicted = predicted;
        }

        @Override
        public void test(T t, List<String> failures) {
            T0 t0;
            try {
                t0 = mapper.apply(t);
            } catch (Exception e) {
                failures.add(key + ": " + e.getMessage());
                return;
            }
            if (predicted.equals(t0)) return;
            failures.add(key + ": " + EqualsTestException.standardMessage(t0, predicted));
        }
    }

    private final List<PredicateCase<T>> cases = new ArrayList<>();

    private PredicateTester() {

    }

    public static <T> PredicateTester<T> of(Class<T> clazz) {
        return new PredicateTester<>();
    }

    public PredicateTester<T> add(String key, TestPredicate<T> predicate) {
        return add(new PredicateCase<>(key, predicate));
    }

    public <T0> PredicateTester<T> add(String key, T0 obj) {
        return add(key, t -> t, obj);
    }

    public <T0> PredicateTester<T> add(String key, TestFunction<T, T0> mapper, T0 obj) {
        return add(new EqualsPredicateCase<>(key, mapper, obj));
    }

    private PredicateTester<T> add(PredicateCase<T> aCase) {
        cases.add(aCase);
        return this;
    }

    public boolean isSuccess(T t) {
        return getFailures(t).isEmpty();
    }

    public List<String> getFailures(T t) {
        List<String> failures = new ArrayList<>();
        for (PredicateCase<T> aCase : cases) {
            aCase.test(t, failures);
        }
        return failures;
    }
}
