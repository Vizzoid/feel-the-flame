package org.vizzoid.utils.test;

public class PredicateTestCase<T> extends TestCase {

    private final TestSupplier<T> supplier;
    private final TestPredicate<T> predicate;

    public PredicateTestCase(TestSupplier<T> supplier, TestPredicate<T> predicate) {
        super(null);
        this.supplier = supplier;
        this.predicate = predicate;
    }

    @Override
    public void testWithException() throws Exception {
        T obj = supplier.get();
        if (!predicate.test(obj)) throw new PredicateTestException(obj);
    }
}
