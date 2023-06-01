package org.vizzoid.zodomorf.generation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class ArrayBuilder<T> {

    public final Function<Double, T> mapper;
    public int width;
    public long seed = ThreadLocalRandom.current().nextLong();

    public static ArrayBuilder<Double> create() {
        return new ArrayBuilder<>(d -> d);
    }

    public static <T> ArrayBuilder<T> map(Function<Double, T> mapper) {
        return new ArrayBuilder<>(mapper);
    }

    private ArrayBuilder(Function<Double, T> mapper) {
        this.mapper = mapper;
    }

    public ArrayBuilder<T> width(int width) {
        this.width = width;
        return this;
    }

    public ArrayBuilder<T> seed(long seed) {
        this.seed = seed;
        return this;
    }

    public ArrayBuilder<T> seed(Random r) {
        return seed(r.nextLong());
    }
}
