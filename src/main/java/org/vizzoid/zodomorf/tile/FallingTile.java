package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;

public class FallingTile implements TileBehavior {

    protected Tile tile;

    public FallingTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        Latice<Tile> latice = tile.getPlanet().getTileLatice();
        int x = tile.getX();
        int y = tile.getY();
        Tile below = latice.get(x, y - 1);
        if (!below.isSolid()) {
            below.swap(tile);
            return;
        }

        if (!tile.left().isSolid()) {
            Tile belowLeft = latice.get(x - 1, y - 1);
            if (!belowLeft.isSolid()) {
                belowLeft.swap(tile);
                return;
            }
            Tile belowLeft1 = latice.get(x - 2, y - 1);
            if (!belowLeft1.isSolid()) {
                belowLeft1.swap(tile);
                return;
            }
        }

        if (!tile.right().isSolid()) {
            Tile belowRight = latice.get(x + 1, y - 1);
            if (!belowRight.isSolid()) {
                belowRight.swap(tile);
                return;
            }
            Tile belowRight1 = latice.get(x + 2, y - 1);
            if (!belowRight1.isSolid()) {
                belowRight1.swap(tile);
                return;
            }
        }

    }

    @Override
    public void update() {

    }
}
