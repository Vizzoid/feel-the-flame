package org.vizzoid.zodomorf.generation;

import java.util.Random;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Material;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.Tile;

public class NormalPlanetGenerator implements PlanetGenerator {

    private static final int MIN_HEIGHT = 475, MAX_HEIGHT = 500,
        MIN_VARIATION_HEIGHT = -3, MAX_VARIATION_HEIGHT = 3,
        SEA_LEVEL = 485, IGNEOUS_HEIGHT = 100;

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
        Tile solid = new Tile(planet);
        solid.setMaterial(Material.FOUNDATION);

        double baseTemperature = planet.getTemperature();

        int width = latice.getWidth();
        Random r = planet.getRandom();
        long heightSeed1 = r.nextLong();
        long heightSeed2 = r.nextLong();
        long heightVariationSeed = r.nextLong();
        for (int x = 0; x < width; x++) {
            double heightNoise1 = OpenSimplex2S.noise2(heightSeed1, x * (1 / 64d), 0);
            double heightNoise2 = OpenSimplex2S.noise2(heightSeed2, x * (1 / 64d), 0);
            double heightNoise = (heightNoise1 + heightNoise2) * 0.5;
            double heightVariationNoise = OpenSimplex2S.noise2(heightVariationSeed, x * (1 / 12d), 0);
            
            int height = (int) (clamp(heightNoise, MIN_HEIGHT, MAX_HEIGHT) + 
                clamp(heightVariationNoise, MIN_VARIATION_HEIGHT, MAX_VARIATION_HEIGHT));
            
            int dirtHeight = height - IGNEOUS_HEIGHT;
            for (int y = dirtHeight; y < height; y++) {
                latice.set(x, y, new Tile(planet, Material.DIRT));
            }
            for (int y = IGNEOUS_HEIGHT; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, Material.SEDIMENTARY_ROCK));
            }
            for (int y = 0; y < IGNEOUS_HEIGHT; y++) {
                latice.set(x, y, new Tile(planet, Material.IGNEOUS_ROCK));
            }
            for (int y = height; y <= SEA_LEVEL; y++) {
                latice.set(x, y, new Tile(planet, sea));
            }
        }

    }

}
