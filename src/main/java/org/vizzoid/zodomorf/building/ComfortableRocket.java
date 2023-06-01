package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Tile;

public class ComfortableRocket extends Rocket {

    public ComfortableRocket(Tile tile) {
        super(tile);
        double minTemp = tile.getPlanet().getMinTemperature();
        iterateTiles(t -> t.setTemperature(minTemp));
    }

    @Override
    public void tick(long ticks) {
        iterateTiles(t -> {
            double temp = t.getTemperature();
            double tempDiff = (temp > 80 ? -1 : 1) * 6;
            if (Math.abs(temp - 80) < 6) temp = 80;
            else temp += tempDiff;
            t.setTemperature(temp);
        });
    }

    @Override
    public BuildingType getType() {
        return BuildingType.COMFORTABLE_ROCKET;
    }

}
