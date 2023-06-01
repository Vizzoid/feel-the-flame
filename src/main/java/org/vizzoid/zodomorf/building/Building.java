package org.vizzoid.zodomorf.building;

import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Building implements TileBehavior {

    protected final CompositeBuilding composite = new CompositeBuilding(this);
    protected Tile tile;

    public Building(Tile tile) {
        this.tile = tile;
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

    public boolean canPlace(Tile tile) {
        return getType().canPlace(tile);
    }

    public void place(Tile tile) {
        getType().place(tile);
    }

    public void iterateWidth(Consumer<Tile> tile) {
        BuildingType type = getType();
        int width = type.getWidth();

        for (int x = 0; x < width; x++) {
            tile.accept(this.tile.relative(x, 0));
        }
    }

    public void iterateHeight(Consumer<Tile> tile) {
        BuildingType type = getType();
        int height = type.getHeight();

        for (int y = 0; y < height; y++) {
            tile.accept(this.tile.relative(0, y));
        }
    }

    public void iterateTiles(Consumer<Tile> tile) {
        BuildingType type = getType();
        int width = type.getWidth();
        int height = type.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tile.accept(this.tile.relative(x, y));
            }
        }
    }

    public abstract BuildingType getType();

    public Tile getTile() {
        return tile;
    }

    public Point getPos() {
        return tile.getPos();
    }

    public CompositeBuilding getComposite() {
        return composite;
    }

    @Override
    public Image getImage() {
        return getType().getImage();
    }
}
