package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.BarrenPlanet;
import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class BarrenPlanetGenerator implements PlanetGenerator {

    @Override
    public int CAVE_DEPTH() {
        return -20;
    }

    @Override
    public double CAVE_FREQUENCY() {
        return 0.0625;
    }

    @Override
    public double ORE_FREQUENCY() {
        return 0.145;
    }

    @Override
    public double ROCK_FREQUENCY() {
        return 0.09;
    }

    @Override
    public int IGNEOUS_HEIGHT() {
        return 80;
    }

    @Override
    public int DIRT_DEPTH() {
        return 0;
    }

    @Override
    public int MIN_VARIATION_HEIGHT() {
        return 0;
    }

    @Override
    public int MAX_VARIATION_HEIGHT() {
        return 0;
    }

    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.EMPTY).dirt(Material.DEBRIS).crust(Material.CLAY).mantle(Material.SILICATE).metal(Material.NICKEL);
    }

    @Override
    public void populate(Planet planet, PlanetTileSet set) {
        int[] heights = surface(planet, set);
        populateCaves(planet, set, heights);

        int[] dirtHeights = heightMap(planet);
        Latice<Tile> latice = planet.getTileLatice();
        int height = latice.getHeight();

        for (int x = 0; x < dirtHeights.length; x++) {
            for (int y = height, y1 = dirtHeights[x] - DIRT_DEPTH(); y > y1; y--) {
                Tile tile = latice.get(x, y);
                if (!tile.isSolid()) continue;
                tile.setMaterial(set.dirt());
            }
        }
        caveBackground(planet, set, heights);
    }

    @Override
    public void generate(Planet planet) {
        planet.setMinTemperature(10);
        planet.setMaxTemperature(210);
        planet.resetTemperature();
        PlanetGenerator.super.generate(planet);
    }

    @Override
    public Planet generate() {
        Planet planet = new BarrenPlanet();
        generate(planet);
        return planet;
    }
}
