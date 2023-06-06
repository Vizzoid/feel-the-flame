package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.BoundaryTile;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class PlanetLatice extends Latice<Tile> {
    public PlanetLatice(Planet planet) {
        super(500, 250);
        fill((x, y) -> new Tile(planet, Material.EMPTY, x, y));
        int width = getWidth();
        int height = getHeight();

        for (int y = 0; y < height; y++) {
            super.set(0, y, new BoundaryTile(planet, 0, y));
        }
        for (int y = 0, endX = width - 1; y < height; y++) {
            super.set(endX, y, new BoundaryTile(planet, endX, y));
        }
        for (int x = 0; x < width; x++) {
            super.set(x, 0, new BoundaryTile(planet, x, 0));
        }
        for (int x = 0, endY = height - 1; x < width; x++) {
            super.set(x, endY, new BoundaryTile(planet, x, endY));
        }
    }

    @Override
    public void set(int x, int y, Tile tile) {
        if (x == 0) return;
        if (y == 0) return;
        super.set(Math.floorMod(x, width), Math.floorMod(y, height), tile);
    }

    @Override
    public Tile get(int x, int y) {
        return super.get(Math.floorMod(x, width), Math.floorMod(y, height));
    }
}
