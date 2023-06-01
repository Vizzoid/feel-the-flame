package org.vizzoid.zodomorf.generation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class LaticeBuilder<T> {

    public final Function<Double, T> mapper;
    public int width;
    public int height;
    public long seed = ThreadLocalRandom.current().nextLong();

    public static LaticeBuilder<Double> create() {
        return new LaticeBuilder<>(d -> d);
    }

    public static <T> LaticeBuilder<T> map(Function<Double, T> mapper) {
        return new LaticeBuilder<>(mapper);
    }

    private LaticeBuilder(Function<Double, T> mapper) {
        this.mapper = mapper;
    }

    public LaticeBuilder<T> width(int width) {
        this.width = width;
        return this;
    }

    public LaticeBuilder<T> height(int height) {
        this.height = height;
        return this;
    }

    public LaticeBuilder<T> dimension(int width, int height) {
        return width(width).height(height);
    }

    public LaticeBuilder<T> seed(long seed) {
        this.seed = seed;
        return this;
    }

    public LaticeBuilder<T> seed(Random r) {
        return seed(r.nextLong());
    }

}
