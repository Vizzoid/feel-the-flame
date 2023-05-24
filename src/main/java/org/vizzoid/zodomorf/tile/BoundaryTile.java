package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.TileInfo;

import java.io.Serial;

public class BoundaryTile extends Tile {

    @Serial
    private static final long serialVersionUID = -3122486500832223806L;

    public BoundaryTile(Planet planet, int x, int y) {
        super(planet, x, y);
        material = Material.DIRT;
    }

    @Override
    public void setBackground(Material background) {

    }

    @Override
    public void setMaterial(Material material, boolean updateBehavior, boolean updateBackground) {

    }

    @Override
    public void setMiddleGround(Material middleGround, boolean updateBehavior) {

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
    public void transitionTemperature(Tile tile) {

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

    @Override
    public boolean isBoundary() {
        return true;
    }

    @Override
    public void setMiddleGround(Material middleGround) {

    }

    @Override
    public void setMaterial(Material material) {

    }

    @Override
    public void setBehavior(TileBehavior behavior) {

    }

    @Override
    public void setMaterial(Material material, boolean updateBehavior) {

    }

    @Override
    public void setMiddleGroundBehavior(TileBehavior middleGroundBehavior) {

    }

    @Override
    public void setPlanet(Planet planet) {
        super.setPlanet(planet);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isLiquid() {
        return false;
    }

    @Override
    public TileBehavior getBehavior() {
        return TileBehavior.EMPTY;
    }

    @Override
    public TileBehavior getMiddleGroundBehavior() {
        return TileBehavior.EMPTY;
    }

    @Override
    public Material getMaterial() {
        return Material.DIRT;
    }

    @Override
    public int getHealth() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Material getBackground() {
        return Material.DIRT;
    }

    @Override
    public Material getMiddleGround() {
        return Material.DIRT;
    }

}
