package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.tile.Material;

public class VolcanoPlanetGenerator implements PlanetGenerator {
    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.LAVA).dirt(Material.ASH).crust(Material.DEBRIS).mantle(Material.OBSIDIAN).metal(Material.EMPTY);
    }
}
