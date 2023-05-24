package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Material;

public class Cooler extends Building {
    public Cooler() {
        super(3, 2);
    }

    @Override
    public void tick(long ticks) {
        double temperature = tile.getTemperature();
        if (temperature < -20) return;
        tile.setTemperature(temperature - ticks);
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD;
    }

    @Override
    public int getCost() {
        return 20;
    }
}
