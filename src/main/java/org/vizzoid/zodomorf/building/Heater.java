package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Tile;

public class Heater extends Building {
    public Heater(Tile tile) {
        super(tile);
    }

    @Override
    public void tick(long ticks) {
        double temperature = tile.getTemperature();
        if (temperature > tile.getPlanet().getMaxTemperature()) return;
        tile.setTemperature(temperature + ticks * 5);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.HEATER;
    }
}
