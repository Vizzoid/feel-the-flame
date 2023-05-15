package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Material;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.Tile;

public class NormalPlanetGenerator implements PlanetGenerator {

    private static final int MIN_HEIGHT = 475, MAX_HEIGHT = 500;

    @Override
    public void generate(Planet planet) {
        Latice<Tile> latice = planet.getTileLatice();
        Tile solid = new Tile(planet);
        solid.setMaterial(Material.FOUNDATION);

        int width = latice.getWidth();
        long seed = planet.getRandom().nextLong();
        for (int x = 0; x < width; x++) {
            double heightNoise = OpenSimplex2S.noise2(seed, x * (1 / 64d), 0);
            int height = (int) ((MAX_HEIGHT - MIN_HEIGHT) * ((heightNoise + 1) * 0.5)) + MIN_HEIGHT;
            for (int y = 0; y < height; y++) {
                latice.set(x, y, solid);
            }
        }

    }

}
