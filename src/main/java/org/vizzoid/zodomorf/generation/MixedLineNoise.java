package org.vizzoid.zodomorf.generation;

public class MixedLineNoise extends LineNoise {

    private final LineNoise noise1;
    private final LineNoise noise2;

    public MixedLineNoise(LineNoise noise1, LineNoise noise2) {
        this.noise1 = noise1;
        this.noise2 = noise2;
    }

    @Override
    public double getRaw(double x) {
        return (noise1.getRaw(x) + noise2.getRaw(x)) * 0.5;
    }
}
