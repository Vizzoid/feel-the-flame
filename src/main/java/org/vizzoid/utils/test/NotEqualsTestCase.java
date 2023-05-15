package org.vizzoid.utils.test;

import java.util.Objects;

public class NotEqualsTestCase extends TestCase {

    private final TestSupplier<?> actual;
    private final Object predicted;

    public NotEqualsTestCase(TestSupplier<?> actual, Object predicted) {
        super(null);
        this.actual = actual;
        this.predicted = predicted;
    }

    @Override
    public void testWithException() throws Exception {
        Object actualObj = actual.get();
        if (Objects.equals(actualObj, predicted)) {
            throw new NotEqualsTestException(actualObj, predicted);
        }
    }
}
