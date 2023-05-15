package org.vizzoid.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public interface BasicBuilder {

    /**
     * Should include checks name Preconditions or etc to make sure all required variables are set
     */
    void check();

    @SuppressWarnings("unchecked")
    default <T> ArrayList<T> createList(T t, T... t1) {
        if (t == null) return null;
        ArrayList<T> list = new ArrayList<>();
        if (t1 != null) {
            list.addAll(Arrays.asList(t1));
        }
        list.add(t);
        return list;
    }

    default <T, T1> ArrayList<T1> createList(Function<T, T1> mapper, T t, T... t1) {
        if (t == null) return null;
        ArrayList<T1> list = new ArrayList<>();
        if (t1 != null) {
            for (T t2 : t1) {
                list.add(mapper.apply(t2));
            }
        }
        list.add(mapper.apply(t));
        return list;
    }

}
