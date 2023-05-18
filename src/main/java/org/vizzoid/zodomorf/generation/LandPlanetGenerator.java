package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.tile.Material;

public class LandPlanetGenerator implements PlanetGenerator {

    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.EMPTY);
    }

}
