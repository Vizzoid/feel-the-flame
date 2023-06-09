package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.entity.ForestDeer;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class ForestPlanetGenerator implements PlanetGenerator {

    private static final double TREE_NOISE = 1;

    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.WATER).dirt(Material.ASH).crust(Material.DIRT).mantle(Material.GRAVEL).metal(Material.GOLD);
    }

    @Override
    public double ROCK_FREQUENCY() {
        return 0.5;
    }

    @Override
    public int[] igneousHeights(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        int height = latice.getHeight();

        int[] igneousHeights = new int[width];
        Arrays.fill(igneousHeights, height - 1);
        return igneousHeights;
    }

    @Override
    public int DIRT_DEPTH() {
        return 2;
    }

    @Override
    public int CAVE_DEPTH() {
        return 5;
    }

    @Override
    public int IGNEOUS_HEIGHT() {
        return 100;
    }

    @Override
    public int[] caveHeights(int[] heights, Planet planet) {
        int[] caveHeights = new int[heights.length];
        for (int x = 0; x < caveHeights.length; x++) {
            caveHeights[x] = (heights[x] - CAVE_DEPTH());
        }
        return caveHeights;
    }

    @Override
    public void surface(int[] heights, Planet planet, PlanetTileSet set) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        for (int x = 0; x < width; x++) {
            int heightX = heights[x];
            heights[x] = heightX;

            int dirtHeight = heightX - DIRT_DEPTH();
            for (int y = dirtHeight; y < heightX; y++) {
                latice.set(x, y, new Tile(planet, set.dirt(), x, y));
            }
            for (int y = 0; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, set.crust(), x, y));
            }
        }
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
        long treeSeed = r.nextLong();
        long animalSeed = r.nextLong();

        for (int x = 0; x < width; x++) {
            for (int y = 0, caveLevel = caveHeights[x]; y < caveLevel; y++) {
                Tile tile = latice.get(x, y);

                Material rock = set.crust();

                double rockNoise = generation(rockSeed, x, y, ROCK_FREQUENCY());
                if (rockNoise <= -0.5 && y < igneousHeights[x]) rock = set.mantle();

                if (y < IGNEOUS_HEIGHT()) {
                    double oreNoise = generation(oreSeed, x, y, ORE_FREQUENCY());
                    if (oreNoise > (0.95 - (0.4 * ((width - (double) y) / width)))) rock = set.metal();
                }

                double noise = generation(caveSeed, x, y, CAVE_FREQUENCY());
                if (noise > 0.1) {
                    if (y < IGNEOUS_HEIGHT()) {
                        if (noise > (0.8 - (((MAX_HEIGHT() - (double) y) * 0.2) / MAX_HEIGHT()))) {
                            tile.setMaterial(set.sea());
                        } else tile.setMaterial(set.caveAir());

                        double treeNoise = generation(treeSeed, x, y, TREE_NOISE);
                        if (treeNoise > 0.8) tile.setMiddleGround(Material.TREE);

                        double forestDeerNoise = generation(animalSeed, x, y, TREE_NOISE);
                        if (forestDeerNoise > 0.8) new ForestDeer(planet, x, y);
                    } else {
                        tile.setMaterial(set.caveAir());
                    }

                    tile.setBackground(rock);
                }
                else tile.setMaterial(rock);
            }

            long burntTreeSeed = planet.getRandom().nextLong();
            for (int y = heights[x], endY = y + r.nextInt(5, 20); y < endY; y++) {
                double noise = heightMap(burntTreeSeed, x, 1);
                if (noise > 0.6) {
                    Tile tile = latice.get(x, y);
                    tile.setMaterial(Material.ASH);
                }
            }
        }
    }

    @Override
    public void generate(Planet planet) {
        planet.setMinTemperature(-20);
        planet.setMaxTemperature(90);
        planet.resetTemperature();
        PlanetGenerator.super.generate(planet);
    }
}
