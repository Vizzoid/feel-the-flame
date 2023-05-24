package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.Material;

public class LandPlanetGenerator implements PlanetGenerator {

    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.EMPTY);
    }

    @Override
    public void generate(Planet planet) {
        planet.setMinTemperature(90);
        planet.setMaxTemperature(200);
        planet.resetTemperature();
        PlanetGenerator.super.generate(planet);
    }

}
