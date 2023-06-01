package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Latice;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FlatNoise {

    private final long seed;
    private double frequencyX = 1;
    private double frequencyY = 1;
    private double max = 1;
    private double min = -1;

    public FlatNoise() {
        this(ThreadLocalRandom.current());
    }

    public FlatNoise(Random r) {
        this(r.nextLong());
    }

    public FlatNoise(long seed) {
        this.seed = seed;
    }

    public FlatNoise frequencyX(double frequencyX) {
        this.frequencyX = frequencyX;
        return this;
    }

    public FlatNoise frequencyY(double frequencyY) {
        this.frequencyY = frequencyY;
        return this;
    }

    public FlatNoise frequency(double frequency) {
        return frequencyX(frequency).frequencyY(frequency);
    }

    public FlatNoise min(double min) {
        this.min = min;
        return this;
    }

    public FlatNoise max(double max) {
        this.max = max;
        return this;
    }


    public FlatNoise range(double min, double max) {
        return min(min).max(max);
    }

    public double get(double x, double y) {
        return clamp(getRaw(x, y));
    }

    public double getRaw(double x, double y) {
        return OpenSimplex2S.noise2_ImproveX(seed, x * frequencyX, y * frequencyY);
    }

    public double clamp(double noise) {
        return (max - min) * ((noise + 1) * 0.5) + min;
    }

    public <T> Latice<T> toLatice(LaticeBuilder<T> builder) {
        Latice<T> latice = new Latice<>(builder.width, builder.height);
        for (int x = 0; x < builder.width; x++) {
            for (int y = 0; y < builder.height; y++) {
                double value = get(x, y);
                latice.set(x, y, builder.mapper.apply(value));
            }
        }
        return latice;
    }

    public VariedFlatNoise varied(Random r) {
        return new VariedFlatNoise(this, r);
    }

    public MixedFlatNoise mixed(FlatNoise noise) {
        return new MixedFlatNoise(this, noise);
    }
    
}
