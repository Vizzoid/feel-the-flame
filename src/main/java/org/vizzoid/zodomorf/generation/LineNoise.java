package org.vizzoid.zodomorf.generation;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LineNoise {

    private final long seed;
    private double frequency = 1;
    private double max = 1;
    private double min = -1;

    public LineNoise() {
        this(ThreadLocalRandom.current());
    }

    public LineNoise(Random r) {
        this(r.nextLong());
    }

    public LineNoise(long seed) {
        this.seed = seed;
    }

    public LineNoise frequency(double frequency) {
        this.frequency = frequency;
        return this;
    }

    public LineNoise min(double min) {
        this.min = min;
        return this;
    }

    public LineNoise max(double max) {
        this.max = max;
        return this;
    }

    public LineNoise range(double min, double max) {
        return min(min).max(max);
    }

    public double get(double x) {
        return clamp(getRaw(x));
    }

    public double getRaw(double x) {
        return OpenSimplex2S.noise2(seed, x * frequency, 0);
    }

    public double clamp(double noise) {
        return (max - min) * ((noise + 1) * 0.5) + min;
    }

    public <T> T[] toArray(ArrayBuilder<T> builder) {
        Object[] t = new Object[builder.width];
        for (int x = 0; x < builder.width; x++) {
            double value = get(x);
            t[x] = builder.mapper.apply(value);
        }
        //noinspection unchecked
        return (T[]) t;
    }


}
