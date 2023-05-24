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
    public int MIN_HEIGHT() {
        return 10;
    }

    @Override
    public int MAX_HEIGHT() {
        return 30;
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

            int heightX = (int) (PlanetGenerator.super.MAX_HEIGHT() +
                    heightVariationNoise);
            heights[x] = heightX;
        }
        return heights;
    }

    @Override
    public void populateCaves(Planet planet, PlanetTileSet set, int[] heights) {
        int[] caveHeights = caveHeights(heights, planet);
        int[] igneousHeights = igneousHeights(planet);
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        Random r = planet.getRandom();

        long caveSeed = r.nextLong();
        long oreSeed = r.nextLong();
        long rockSeed = r.nextLong();

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
                    if (noise > (0.8 - (((PlanetGenerator.super.MAX_HEIGHT() - (double) y) * 0.2) / PlanetGenerator.super.MAX_HEIGHT()))) tile.setMaterial(set.sea());
                    else tile.setMaterial(set.caveAir());
                    tile.setBackground(rock);
                }
                else tile.setMaterial(rock);
            }
        }

        int[] sand = PlanetGenerator.super.heightMap(planet);
        for (int x = 0; x < width; x++) {
            int heightY = sand[x];

            for (int y = 0; y < heightY; y++) {
                latice.get(x, y).setMaterial(Material.SAND);
            }
        }

        int coralSpace = width / 9;
        int coralVariation = coralSpace / 2;

        for (int x = 1; x < 9; x++) {
            int coralX = (x * coralSpace) + (r.nextInt(coralSpace) - coralVariation);

            latice.get(coralX, sand[coralX]).setMiddleGround(Material.CORAL);
        }
    }

    @Override
    public void generate(Planet planet) {
        planet.setMinTemperature(-90);
        planet.setMaxTemperature(60);
        planet.resetTemperature();
        PlanetGenerator.super.generate(planet);
    }
}
