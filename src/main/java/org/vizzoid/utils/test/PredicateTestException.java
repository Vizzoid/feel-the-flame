package org.vizzoid.utils.test;

import java.io.Serial;
import java.util.List;

public class PredicateTestException extends TestException {
    @Serial
    private static final long serialVersionUID = 4485450982884492095L;

    public PredicateTestException(Object actual) {
        super(standardMessage(actual));
    }

    public PredicateTestException(Object actual, List<String> failures) {
        super(standardMessage(actual) + "s: Predicates with keys " + failures + " failed");
    }

    private static String standardMessage(Object actual) {
        return "Object '" + actual + "' did not match predicate";
    }

}
