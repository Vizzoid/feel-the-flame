package org.vizzoid.utils.test;

@FunctionalInterface
public interface TestFunction<T, T0> {

    T0 apply(T t) throws Exception;

}
