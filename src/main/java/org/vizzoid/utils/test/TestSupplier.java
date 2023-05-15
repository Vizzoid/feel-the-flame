package org.vizzoid.utils.test;

@FunctionalInterface
public interface TestSupplier<T> {

    T get() throws Exception;

}
