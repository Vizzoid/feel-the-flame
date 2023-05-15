package org.vizzoid.utils.test;

import java.io.Serial;

public class NotEqualsTestException extends TestException {
    @Serial
    private static final long serialVersionUID = 4308082442752854108L;

    public NotEqualsTestException(Object actual, Object predicted) {
        super("Object '" + actual + "' matched predicted '" + predicted + "'");
    }
}
