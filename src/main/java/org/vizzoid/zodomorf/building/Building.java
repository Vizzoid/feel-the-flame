package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

public abstract class Building implements TileBehavior {

    private final int width;
    private final int height;
    protected Tile tile;

    public Building(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canPlace(Tile tile) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (avatar.getStorage(getMaterial()) < getCost()) return false;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                if (relative.isSolid() || relative.getMiddleGround().isSolid()) return false;
            }
        }
        return true;
    }

    public void place(Tile tile) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (avatar.spend(getMaterial(), getCost())) return;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                relative.setMiddleGround(Material.COMPOSITE);
            }
        }
        tile.setMiddleGround(Material.BUILDING);
        tile.setMiddleGroundBehavior(this);
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

    public abstract Material getMaterial();

    public abstract int getCost();

}
