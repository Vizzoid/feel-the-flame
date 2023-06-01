package org.vizzoid.zodomorf.generation;

import org.vizzoid.utils.Optional;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.entity.EntityType;
import org.vizzoid.zodomorf.tile.Material;

public class DesertPlanetGenerator implements PlanetGenerator {
    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.EMPTY).dirt(Material.SAND).crust(Material.SANDSTONE).mantle(Material.SANDSTONE).metal(Material.SANDSTONE);
    }

    @Override
    public void generate(Planet planet) {
        planet.setMinTemperature(80);
        planet.setMaxTemperature(280);
        planet.resetTemperature();
        PlanetGenerator.super.generate(planet);
    }

    private static final Optional<Material> PLANT = Optional.of(Material.CACTUS);
    private static final Optional<EntityType> ANIMAL = Optional.of(EntityType.CACTUS_BEAR);

    @Override
    public Optional<Material> plant() {
        return PLANT;
    }

    @Override
    public Optional<EntityType> animal() {
        return ANIMAL;
    }
}
