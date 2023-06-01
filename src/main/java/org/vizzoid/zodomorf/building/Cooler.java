package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Tile;

public class Cooler extends Building {
    public Cooler(Tile tile) {
        super(tile);
    }

    @Override
    public void tick(long ticks) {
        double temperature = tile.getTemperature();
        if (temperature < tile.getPlanet().getMinTemperature()) return;
        tile.setTemperature(temperature - ticks * 5);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.COOLER;
    }
}
