package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

public class Tile implements TilePainter {

    private final Planet planet;
    private double temperature;
    private Material background = Material.EMPTY;
    private Material material = Material.EMPTY;

    public Tile(Planet planet) {
        this.planet = planet;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public Material getBackground() {
        return this.background;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setBackground(Material background) {
        this.background = background;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public double transitionTemperature(double temperature, long ticks) {
        return planet.transitionTemperature(this.temperature, temperature, ticks);
    }

    public void tick(long ticks) {
        if (background.isEmpty()) {
            temperature = planet.transitionTemperature(this.temperature, ticks);
        }
    }

    @Override
    public void paint(TileInfo info) {
        material.paint(info);
        background.paintBackground(info);
    }
}
