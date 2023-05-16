package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.engine.TileInfo;

public class BoundaryTile extends Tile {
    public BoundaryTile(Planet planet, int x, int y) {
        super(planet, x, y);
        material = Material.DIRT;
    }

    @Override
    public void setBackground(Material background) {

    }

    @Override
    public void setMaterial(Material material) {

    }

    @Override
    public void setTemperature(double temperature) {

    }

    @Override
    public double getTemperature() {
        return planet.getTemperature();
    }

    @Override
    public void tick(long ticks) {

    }

    @Override
    public void paint(TileInfo info) {
        super.paint(info);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean canMoveInto() {
        return false;
    }
}
