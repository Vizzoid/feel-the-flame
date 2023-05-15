package org.vizzoid.utils.test;

public class TestCase {

    private final Tester testFunction;

    /**
     * @param testFunction testable function, can be null if testWithException is overridden
     */
    public TestCase(Tester testFunction) {
        this.testFunction = testFunction;
    }

    public final TestResult test() {
        TestResult result;
        try {
            testWithException();
            result = TestResult.success();
        } catch (Exception e) {
            result = TestResult.failure(e);
        }
        return result;
    }

    /**
     * Run test, any unacceptable or unexpected behavior should have an error thrown
     * @throws TestException if any behavior is unprecedented
     */
    public void testWithException() throws Exception {
        if (testFunction == null) throw new IllegalArgumentException("Tester is null but method is not overridden (TestCase exception)");
        testFunction.test();
    }

    public static TestCase create(Tester function) {
        return new TestCase(function);
    }

    public static EqualsTestCase equals(TestSupplier<?> actual, Object predicted) {
        return new EqualsTestCase(actual, predicted);
    }

    public static NotEqualsTestCase notEquals(TestSupplier<?> actual, Object predicted) {
        return new NotEqualsTestCase(actual, predicted);
    }

    public static <T> PredicateTestCase<T> predicate(TestSupplier<T> supplier, TestPredicate<T> predicate) {
        return new PredicateTestCase<>(supplier, predicate);
    }

    public static <T> PredicateResultCase<T> predicate(TestSupplier<T> supplier, PredicateTester<T> result) {
        return new PredicateResultCase<>(supplier, result);
    }

    public static BooleanTestCase bool(TestSupplier<Boolean> supplier) {
        return new BooleanTestCase(supplier);
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj;
    }
}
