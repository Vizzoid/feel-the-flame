package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.*;
import org.vizzoid.zodomorf.tile.BoundaryTile;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Random;

public class NormalPlanetGenerator implements PlanetGenerator {

    private static final int MIN_HEIGHT = 120, MAX_HEIGHT = 150,
        MIN_VARIATION_HEIGHT = -3, MAX_VARIATION_HEIGHT = 3,
        SEA_LEVEL = 130, DIRT_DEPTH = 5, IGNEOUS_HEIGHT = 50,
        CAVE_DEPTH = 20;
    private static final double HEIGHT_FREQUENCY = 1/64d, HEIGHT_VARIATION_FREQUENCY = 1/12d,
            CAVE_FREQUENCY = 0.125, ROCK_FREQUENCY = 0.15, ORE_FREQUENCY = 0.125;

    private static double clamp(double noise, double min, double max) {
        return (max - min) * ((noise + 1) * 0.5) + min;
    }

    private final PlanetTileSet set;

    public NormalPlanetGenerator(PlanetTileSet set) {
        this.set = set;
    }

    @Override
    public void generate(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        int height = latice.getHeight();
        Random r = planet.getRandom();

        long heightSeed1 = r.nextLong();
        long heightSeed2 = r.nextLong();
        long heightVariationSeed = r.nextLong();
        long caveSeed = r.nextLong();
        long rockSeed = r.nextLong();
        long igneousHeightSeed = r.nextLong();
        long caveHeightSeed = r.nextLong();

        int[] heights = new int[width];
        for (int x = 0; x < width; x++) {
            double heightNoise1 = OpenSimplex2S.noise2(heightSeed1, x * HEIGHT_FREQUENCY, 0);
            double heightNoise2 = OpenSimplex2S.noise2(heightSeed2, x * HEIGHT_FREQUENCY, 0);
            double heightNoise = (heightNoise1 + heightNoise2) * 0.5;
            double heightVariationNoise = OpenSimplex2S.noise2(heightVariationSeed, x * HEIGHT_VARIATION_FREQUENCY, 0);
            
            int heightX = (int) (clamp(heightNoise, MIN_HEIGHT, MAX_HEIGHT) +
                clamp(heightVariationNoise, MIN_VARIATION_HEIGHT, MAX_VARIATION_HEIGHT));
            heights[x] = heightX;
            
            int dirtHeight = heightX - DIRT_DEPTH;
            for (int y = dirtHeight; y < heightX; y++) {
                latice.set(x, y, new Tile(planet, set.dirt(), x, y));
            }
            for (int y = 0; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, set.crust(), x, y));
            }
            for (int y = heightX; y <= SEA_LEVEL; y++) {
                latice.set(x, y, new Tile(planet, set.sea(), x, y));
            }
        }

        int[] caveHeights = new int[heights.length];
        for (int x = 0; x < caveHeights.length; x++) {
            double caveHeight = OpenSimplex2S.noise2(caveHeightSeed, x * HEIGHT_FREQUENCY, 0);
            caveHeights[x] = (int) ((heights[x] - CAVE_DEPTH) + clamp(caveHeight, -5, 5));
        }

        int[] igneousHeights = new int[heights.length];
        for (int x = 0; x < igneousHeights.length; x++) {
            double igneousHeight = OpenSimplex2S.noise2(igneousHeightSeed, x * HEIGHT_FREQUENCY, 0);
            igneousHeights[x] = (int) (IGNEOUS_HEIGHT + clamp(igneousHeight, -10, 10));
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0, caveLevel = caveHeights[x]; y < caveLevel; y++) {
                Tile tile = latice.get(x, y);

                Material rock = set.crust();

                double rockNoise = OpenSimplex2S.noise2_ImproveX(rockSeed, x * ROCK_FREQUENCY, y * ROCK_FREQUENCY);
                if (rockNoise <= 0 && y < igneousHeights[x]) rock = set.mantle();

                double oreNoise = OpenSimplex2S.noise2_ImproveX(rockSeed, x * ORE_FREQUENCY, y * ORE_FREQUENCY);
                if (oreNoise > 0.55) rock = set.metal();

                double noise = OpenSimplex2S.noise2_ImproveX(caveSeed, x * CAVE_FREQUENCY, y * CAVE_FREQUENCY);
                if (noise > 0.1) {
                    if (noise > (0.8 - (((MAX_HEIGHT - (double) y) * 0.2) / MAX_HEIGHT))) tile.setMaterial(set.sea());
                    else tile.setMaterial(set.caveAir());
                    tile.setBackground(rock);
                }
                else tile.setMaterial(rock);
            }
        }

        for (int y = 0; y < height; y++) {
            latice.set(0, y, new BoundaryTile(planet, 0, y));
        }
        for (int y = 0, endX = width - 1; y < height; y++) {
            latice.set(endX, y, new BoundaryTile(planet, endX, y));
        }
        for (int x = 0; x < width; x++) {
            latice.set(x, 0, new BoundaryTile(planet, x, 0));
        }
        for (int x = 0, endY = height - 1; x < width; x++) {
            latice.set(x, endY, new BoundaryTile(planet, x, endY));
        }

        for (int i = 0; i < 1000; i++) { // "settle" the liquids (please find something that works better)
            for (Tile tile : latice) {
                if (!tile.isLiquid()) continue;
                tile.tick(1);
            }
        }

    }

}
