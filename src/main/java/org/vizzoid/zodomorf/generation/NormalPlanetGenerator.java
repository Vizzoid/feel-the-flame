package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.*;

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

    private final Material sea;

    public NormalPlanetGenerator(Material sea) {
        this.sea = sea;
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
                latice.set(x, y, new Tile(planet, Material.DIRT, x, y));
            }
            for (int y = 0; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, Material.SEDIMENTARY_ROCK, x, y));
            }
            for (int y = heightX; y <= SEA_LEVEL; y++) {
                latice.set(x, y, new Tile(planet, sea, x, y));
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0, caveLevel = heights[x] - CAVE_DEPTH; y < caveLevel; y++) {
                Tile tile = latice.get(x, y);
                if (!tile.getMaterial().isRock()) continue;
                double noise = OpenSimplex2S.noise2_ImproveX(caveSeed, x * CAVE_FREQUENCY, y * CAVE_FREQUENCY);
                if (noise > 0.1) {
                    if (noise > (0.8 - (((MAX_HEIGHT - (double) y) * 0.2) / MAX_HEIGHT))) tile.setMaterial(Material.LAVA);
                    else tile.setMaterial(Material.EMPTY);
                    continue;
                }
                double rockNoise = OpenSimplex2S.noise2_ImproveX(rockSeed, x * ROCK_FREQUENCY, y * ROCK_FREQUENCY);
                if (rockNoise <= 0 && y < IGNEOUS_HEIGHT) tile.setMaterial(Material.IGNEOUS_ROCK);
                else tile.setMaterial(Material.SEDIMENTARY_ROCK);

                double oreNoise = OpenSimplex2S.noise2_ImproveX(rockSeed, x * ORE_FREQUENCY, y * ORE_FREQUENCY);
                if (oreNoise > 0.55) latice.set(x, y, new Tile(planet, Material.COPPER_ORE, x, y));
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

        for (Tile tile : latice) {
            tile.settle();
        }
    }

}
