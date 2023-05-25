package org.vizzoid.zodomorf.building;

import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

import java.awt.*;

public abstract class Building implements TileBehavior, Buildable, Cloneable {

    protected final CompositeBuilding composite = new CompositeBuilding(this);
    protected final int width;
    protected final int height;
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
        for (int x = 0; x < width; x++) {
            Tile relative = tile.relative(x, -1);
            if (!relative.isSolid()) return false;
        }
        return true;
    }

    public void place(Tile tile) {
        Avatar avatar = tile.getPlanet().getAvatar();
        avatar.spend(getMaterial(), getCost());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                relative.setMaterial(Material.EMPTY);
                relative.setMiddleGround(Material.COMPOSITE);
                relative.setMiddleGroundBehavior(composite);
            }
        }
        Building cloned = clone();
        tile.setMiddleGroundBehavior(this);
        tile.setMiddleGround(Material.BUILDING, false);
        tile.getPlanet().getAvatar().setBuildable(cloned);
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

    @Override
    public abstract Image getImage();

    public abstract Material getMaterial();

    public abstract int getCost();

    @Override
    public Building clone() {
        try {
            return (Building) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Tile getTile() {
        return tile;
    }

    public Point getPos() {
        return tile.getPos();
    }

}
