package org.vizzoid.utils.test;

import java.util.Objects;

public class EqualsTestCase extends TestCase {

    private final TestSupplier<?> actual;
    private final Object predicted;

    public EqualsTestCase(TestSupplier<?> actual, Object predicted) {
        super(null);
        this.actual = actual;
        this.predicted = predicted;
    }

    @Override
    public void testWithException() throws Exception {
        Object actualObj = actual.get();
        if (predicted instanceof EqualsObject) {
            EqualsObject actualEquals = (EqualsObject) predicted;
            EqualsResult result = actualEquals.testEquals(actualObj);
            if (!result.isSuccess()) throw new EqualsTestException(actualObj, predicted, result);
            return;
        }
        if (!Objects.equals(actualObj, predicted)) {
            throw new EqualsTestException(actualObj, predicted);
        }
    }
}
