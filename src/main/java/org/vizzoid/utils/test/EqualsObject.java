package org.vizzoid.utils.test;

import java.util.Objects;

public interface EqualsObject {

    EqualsResult testEquals(Object o);

    default void testOrRegister(String fieldName, EqualsResult result, Object actual, Object predicted) {
        if (Objects.equals(actual, predicted)) return;
        result.addFailure(fieldName, actual, predicted);
    }

}
