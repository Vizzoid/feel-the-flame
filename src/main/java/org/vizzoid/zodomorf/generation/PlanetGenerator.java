package org.vizzoid.zodomorf.generation;

import org.vizzoid.utils.Optional;
import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.entity.EntityType;
import org.vizzoid.zodomorf.tile.BoundaryTile;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Random;

public interface PlanetGenerator {

    default int MIN_HEIGHT() { return 120;}
    default int MAX_HEIGHT() { return 150;}
    default int SEA_LEVEL() { return 130;}
    default int MIN_VARIATION_HEIGHT() { return -3;}
    default int MAX_VARIATION_HEIGHT() { return 3;}
    default int DIRT_DEPTH() { return 5;}
    default int IGNEOUS_HEIGHT() { return 50;}
    default int CAVE_DEPTH() { return 20;}
    default double HEIGHT_FREQUENCY() { return 0.015625;}
    default double HEIGHT_VARIATION_FREQUENCY() { return 0.08333333333333333;}
    default double CAVE_FREQUENCY() { return 0.125;}
    default double ROCK_FREQUENCY() { return 0.15;}
    default double ORE_FREQUENCY() { return 0.125;}
    default double PLANT_NOISE() { return 1;}
    default double ANIMAL_NOISE() { return 1;}
    default Optional<Material> plant() { return Optional.empty();}
    default Optional<EntityType> animal() { return Optional.empty();}

    PlanetTileSet set();

    default void generate(Planet planet) {
        complete(planet, set());
    }

    default double clamp(double noise, double min, double max) {
        return (max - min) * ((noise + 1) * 0.5) + min;
    }

    default double heightMap(long seed, double x, double frequency) {
        return OpenSimplex2S.noise2(seed, x * frequency, 0);
    }

    default double generation(long seed, double x, double y, double frequency) {
        return OpenSimplex2S.noise2_ImproveX(seed, x * frequency, y * frequency);
    }

    default double heightMap(long seed, double x, double frequency, double min, double max) {
        return clamp(heightMap(seed, x, frequency), min, max);
    }

    default double generation(long seed, double x, double y, double frequency, double min, double max) {
        return clamp(generation(seed, x, y, frequency), min, max);
    }

    default int[] heightMap(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        Random r = planet.getRandom();

        long heightSeed1 = r.nextLong();
        long heightSeed2 = r.nextLong();
        long heightVariationSeed = r.nextLong();
        int[] heights = new int[width];
        for (int x = 0; x < width; x++) {
            double heightNoise1 = heightMap(heightSeed1, x, HEIGHT_FREQUENCY());
            double heightNoise2 = heightMap(heightSeed2, x, HEIGHT_FREQUENCY());
            double heightNoise = (heightNoise1 + heightNoise2) * 0.5;
            double heightVariationNoise = heightMap(heightVariationSeed, x, HEIGHT_VARIATION_FREQUENCY(), MIN_VARIATION_HEIGHT(), MAX_VARIATION_HEIGHT());

            int heightX = (int) (clamp(heightNoise, MIN_HEIGHT(), MAX_HEIGHT()) +
                    heightVariationNoise);
            heights[x] = heightX;
        }
        return heights;
    }

    default int[] surface(Planet planet, PlanetTileSet set) {
        int[] heights = heightMap(planet);
        surface(heights, planet, set);
        return heights;
    }

    default void surface(int[] heights, Planet planet, PlanetTileSet set) {
        Latice<Tile> latice = planet.getTileLatice();
        latice.fill((x, y) -> new Tile(planet, x, y));

        int width = latice.getWidth();
        for (int x = 0; x < width; x++) {
            int heightY = heights[x];

            int dirtHeight = heightY - DIRT_DEPTH();
            for (int y = dirtHeight; y < heightY; y++) {
                latice.set(x, y, new Tile(planet, set.dirt(), x, y));
            }
            for (int y = 0; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, set.crust(), x, y));
            }
            for (int y = heightY; y <= SEA_LEVEL(); y++) {
                latice.set(x, y, new Tile(planet, set.sea(), x, y));
            }
        }
    }

    default int[] caveHeights(int[] heights, Planet planet) {
        int[] caveHeights = new int[heights.length];
        long caveHeightSeed = planet.getRandom().nextLong();
        for (int x = 0; x < caveHeights.length; x++) {
            double caveHeight = heightMap(caveHeightSeed, x, HEIGHT_FREQUENCY(), -5, 5);
            caveHeights[x] = (int) ((heights[x] - CAVE_DEPTH()) + caveHeight);
        }
        return caveHeights;
    }

    default int[] igneousHeights(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();

        int[] igneousHeights = new int[width];
        long igneousHeightSeed = planet.getRandom().nextLong();
        for (int x = 0; x < igneousHeights.length; x++) {
            double igneousHeight = heightMap(igneousHeightSeed, x, HEIGHT_FREQUENCY(), -10, 10);
            igneousHeights[x] = (int) (IGNEOUS_HEIGHT() + igneousHeight);
        }
        return igneousHeights;
    }

    default void populate(Planet planet, PlanetTileSet set) {
        int[] heights = surface(planet, set);
        populateCaves(planet, set, heights);
        caveBackground(planet, set, heights);
    }

    default void populateCaves(Planet planet, PlanetTileSet set, int[] heights) {
        int[] caveHeights = caveHeights(heights, planet);
        int[] igneousHeights = igneousHeights(planet);
        populateCaves(planet, set, caveHeights, igneousHeights);
    }

    default void populateCaves(Planet planet, PlanetTileSet set, int[] caveHeights, int[] igneousHeights) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        Random r = planet.getRandom();

        long caveSeed = r.nextLong();
        long oreSeed = r.nextLong();
        long rockSeed = r.nextLong();
        long plantSeed = r.nextLong();
        long animalSeed = r.nextLong();

        for (int x = 0; x < width; x++) {
            for (int y = 0, caveLevel = caveHeights[x]; y < caveLevel; y++) {
                Tile tile = latice.get(x, y);

                Material rock = set.crust();

                double rockNoise = generation(rockSeed, x, y, ROCK_FREQUENCY());
                if (rockNoise <= 0 && y < igneousHeights[x]) rock = set.mantle();

                double oreNoise = generation(oreSeed, x, y, ORE_FREQUENCY());
                if (oreNoise > 0.55) rock = set.metal();

                double noise = generation(caveSeed, x, y, CAVE_FREQUENCY());
                if (noise > 0.1) {
                    if (y < IGNEOUS_HEIGHT()) {
                        if (noise > (0.8 - (((MAX_HEIGHT() - (double) y) * 0.2) / MAX_HEIGHT()))) tile.setMaterial(set.sea());
                        else tile.setMaterial(set.caveAir());

                        Optional<Material> plant = plant();
                        if (plant.isPresent()) {
                            double plantNoise = generation(plantSeed, x, y, PLANT_NOISE());
                            if (plantNoise > 0.8) {
                                Tile spawn = tile.below();
                                while (!spawn.isSolid()) spawn = spawn.below();
                                spawn.above().setMiddleGround(plant.getValue());
                            }
                        }

                        Optional<EntityType> animal = animal();
                        if (animal.isPresent()) {
                            double animalNoise = generation(animalSeed, x, y, ANIMAL_NOISE());
                            if (animalNoise > 0.8) {
                                Tile spawn = tile.below();
                                while (!spawn.isSolid()) spawn = spawn.below();
                                animal.getValue().create(planet, x, spawn.getY());
                            }
                        }
                    } else {
                        tile.setMaterial(set.caveAir());
                    }
                    tile.setBackground(rock);
                }
                else tile.setMaterial(rock);
            }
        }
    }

    default void boundary(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        int height = latice.getHeight();

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
    }

    default void settle(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();
        for (int i = 0; i < 1000; i++) { // "settle" the liquids (please find something that works better)
            for (Tile tile : latice) {
                //if (!tile.isLiquid()) continue;
                tile.tick(1);
            }
        }
    }

    default void complete(Planet planet, PlanetTileSet set) {
        populate(planet, set);
        boundary(planet);
        settle(planet);
    }

    default void caveBackground(Planet planet, PlanetTileSet set, int[] heights) {
        Latice<Tile> latice = planet.getTileLatice();
        Material material = set.caveBackground();
        for (int x = 0; x < heights.length; x++) {
            for (int y = 0; y < heights[x]; y++) {
                Tile tile = latice.get(x, y);
                if (tile.getBackground().isSolid()) continue;
                tile.setBackground(material);
            }
        }
    }

    default Planet generate() {
        Planet planet = new Planet();
        generate(planet);
        return planet;
    }
}
