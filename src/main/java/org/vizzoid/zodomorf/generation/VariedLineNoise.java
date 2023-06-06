package org.vizzoid.zodomorf.generation;

import java.util.Random;

public class VariedLineNoise extends LineNoise {

    private final LineNoise noise;
    private final LineNoise varied;

    public VariedLineNoise(LineNoise noise, Random r) {
        this(noise, new LineNoise(r));
    }

    public VariedLineNoise(LineNoise noise, LineNoise varied) {
        this.noise = noise;
        this.varied = varied;
    }

    @Override
    public double get(double x) {
        return noise.get(x) + varied.get(x);
    }

    public VariedLineNoise varied(double varied) {
        return variedMin(-varied).variedMax(varied);
    }

    public VariedLineNoise variedMax(double variedMax) {
        varied.max(variedMax);
        return this;
    }

    public VariedLineNoise variedMin(double variedMin) {
        varied.min(variedMin);
        return this;
    }

    public VariedLineNoise variedRange(double min, double max) {
        return variedMin(min).variedMax(max);
    }

}
