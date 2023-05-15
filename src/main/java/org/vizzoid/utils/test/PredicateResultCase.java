package org.vizzoid.utils.test;

import java.util.List;

public class PredicateResultCase<T> extends TestCase {

    private final TestSupplier<T> supplier;
    private final PredicateTester<T> result;

    public PredicateResultCase(TestSupplier<T> supplier, PredicateTester<T> result) {
        super(null);
        this.supplier = supplier;
        this.result = result;
    }

    @Override
    public void testWithException() throws Exception {
        T obj = supplier.get();
        List<String> failures = result.getFailures(obj);
        if (!failures.isEmpty()) throw new PredicateTestException(obj, failures);
    }
}
