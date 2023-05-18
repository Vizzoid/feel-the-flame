package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Random;

public class OceanPlanetGenerator implements PlanetGenerator {
    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.ICE).dirt(Material.OBSIDIAN).crust(Material.WATER).mantle(Material.WATER).metal(Material.WATER).caveAir(Material.WATER);
    }

    @Override
    public int MIN_VARIATION_HEIGHT() {
        return -1;
    }

    @Override
    public int MAX_VARIATION_HEIGHT() {
        return 1;
    }

    @Override
    public int DIRT_DEPTH() {
        return 20;
    }

    @Override
    public int[] heightMap(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        Random r = planet.getRandom();

        long heightVariationSeed = r.nextLong();
        int[] heights = new int[width];
        for (int x = 0; x < width; x++) {
            double heightVariationNoise = heightMap(heightVariationSeed, x, HEIGHT_VARIATION_FREQUENCY(), MIN_VARIATION_HEIGHT(), MAX_VARIATION_HEIGHT());

            int heightX = (int) (MAX_HEIGHT() +
                    heightVariationNoise);
            heights[x] = heightX;
        }
        return heights;
    }
}
