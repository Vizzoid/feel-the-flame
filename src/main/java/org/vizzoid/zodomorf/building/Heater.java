package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Material;

public class Heater extends Building {
    public Heater() {
        super(3, 2);
    }

    @Override
    public void tick(long ticks) {
        double temperature = tile.getTemperature();
        if (temperature > 270) return;
        tile.setTemperature(temperature + ticks);
    }

    @Override
    public Material getMaterial() {
        return Material.COPPER_ORE;
    }

    @Override
    public int getCost() {
        return 20;
    }
}
