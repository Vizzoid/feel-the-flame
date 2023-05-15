package org.vizzoid.utils.test;

import java.util.HashMap;
import java.util.Map;

public class EqualsResult {

    private static final EqualsResult NOT_INSTANCE = new EqualsResult() {
        @Override
        public String toString() {
            return "[Object was not an instance]";
        }
    };
    private static final EqualsResult SUCCESS = new EqualsResult() {
        @Override
        public void addFailure(String fieldName, Object actual, Object predicted) {

        }
    };

    private final Map<String, EqualsFieldCase> failures = new HashMap<>();

    private EqualsResult() {

    }

    public static EqualsResult create() {
        return new EqualsResult();
    }

    public static EqualsResult success() {
        return SUCCESS;
    }

    public static EqualsResult notInstance() {
        return NOT_INSTANCE;
    }

    public void addFailure(String fieldName, Object actual, Object predicted) {
        failures.put(fieldName, new EqualsFieldCase(actual, predicted));
    }

    public boolean isSuccess() {
        return failures.isEmpty();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("\n\t[");

        var iterator = failures.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            String s = entry.getKey();
            EqualsFieldCase e = entry.getValue();
            builder
                .append("Field '")
                .append(s)
                .append("' had Object '")
                .append(e.actual)
                .append("' when it desired '")
                .append(e.predicted)
                .append("'");

            if (iterator.hasNext()) {
                builder.append(", \n\t");
            }
        }

        return builder.append("]").toString();
    }

    private static class EqualsFieldCase {
        public final Object actual;
        public final Object predicted;

        private EqualsFieldCase(Object actual, Object predicted) {
            this.actual = actual;
            this.predicted = predicted;
        }
    }

}
