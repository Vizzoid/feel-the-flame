package org.vizzoid.zodomorf.generation;

public class MixedFlatNoise extends FlatNoise {

    private final FlatNoise noise1;
    private final FlatNoise noise2;

    public MixedFlatNoise(FlatNoise noise1, FlatNoise noise2) {
        this.noise1 = noise1;
        this.noise2 = noise2;
    }

    @Override
    public double getRaw(double x, double y) {
        return (noise1.getRaw(x, y) + noise2.getRaw(x, y)) * 0.5;
    }
}
