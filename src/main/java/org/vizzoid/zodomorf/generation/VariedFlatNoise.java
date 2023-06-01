package org.vizzoid.zodomorf.generation;

import java.util.Random;

public class VariedFlatNoise extends FlatNoise {

    private final FlatNoise noise;
    private final FlatNoise varied;

    public VariedFlatNoise(FlatNoise noise, Random r) {
        this.noise = noise;
        varied = new FlatNoise(r);
    }

    @Override
    public double get(double x, double y) {
        return noise.get(x, y) + varied.get(x, y);
    }

    public VariedFlatNoise varied(double varied) {
        return variedMin(-varied).variedMax(varied);
    }

    public VariedFlatNoise variedMax(double variedMax) {
        varied.max(variedMax);
        return this;
    }

    public VariedFlatNoise variedMin(double variedMin) {
        varied.min(variedMin);
        return this;
    }

    public VariedFlatNoise variedRange(double min, double max) {
        return variedMin(min).variedMax(max);
    }

}
