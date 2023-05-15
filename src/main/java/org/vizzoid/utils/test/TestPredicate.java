package org.vizzoid.utils.test;

@FunctionalInterface
public interface TestPredicate<T> {

    boolean test(T t);

}
