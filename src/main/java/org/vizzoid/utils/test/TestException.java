package org.vizzoid.utils.test;

import java.io.Serial;

public class TestException extends Exception {
    @Serial
    private static final long serialVersionUID = -5811496235247467014L;

    public TestException(String message) {
        super(message);
    }
}
