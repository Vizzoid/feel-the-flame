package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

public class Building implements TileBehavior {

    private final int width;
    private final int height;
    private Tile tile = Tile.EMPTY;

    public Building(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canPlace(Tile tile) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                if (relative.isSolid() || relative.getMiddleGround().isSolid()) return false;
            }
        }
        return true;
    }

    public void place(Tile tile) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                relative.setMiddleGround(Material.COMPOSITE);
            }
        }
        tile.setMiddleGround(Material.BUILDING);
        tile.setBehavior(this);
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {

    }

    @Override
    public void update() {

    }
}
