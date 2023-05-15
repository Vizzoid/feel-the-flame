package org.vizzoid.utils.test;

import java.io.Serial;

public class EqualsTestException extends TestException {

    @Serial
    private static final long serialVersionUID = 2076434565603793126L;

    public EqualsTestException(Object actual, Object predicted) {
        super(standardMessage(actual, predicted));
    }

    public EqualsTestException(Object actual, Object predicted, EqualsResult result) {
        super(standardMessage(actual, predicted) + ": " + result);
    }

    public static String standardMessage(Object actual, Object predicted) {
        return "Object '" + actual + "' did not match predicted '" + predicted + "'";
    }

}
