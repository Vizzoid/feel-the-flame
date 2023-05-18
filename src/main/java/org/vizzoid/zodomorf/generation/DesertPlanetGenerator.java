package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.tile.Material;

public class DesertPlanetGenerator implements PlanetGenerator {
    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.EMPTY).dirt(Material.SAND).crust(Material.SANDSTONE).mantle(Material.SANDSTONE).metal(Material.SANDSTONE);
    }
}
