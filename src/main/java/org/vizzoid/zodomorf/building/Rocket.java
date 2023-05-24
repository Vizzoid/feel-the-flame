package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Material;

public class Rocket extends Building {

    public Rocket() {
        super(3, 8);
    }

    @Override
    public Material getMaterial() {
        return Material.NICKEL;
    }

    @Override
    public int getCost() {
        return 30;
    }
}
