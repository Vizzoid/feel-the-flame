package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class ComfortableRocket extends Rocket {

    @Override
    public Material getMaterial() {
        return Material.NICKEL;
    }

    @Override
    public int getCost() {
        return 30;
    }

    @Override
    public String getName() {
        return "Comfortable Rocket";
    }

    @Override
    public void tick(long ticks) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                double temp = relative.getTemperature();
                double tempDiff = (temp > 80 ? -1 : 1) * 6;
                if (Math.abs(temp - 80) < 6) temp = 80;
                relative.setTemperature(temp + tempDiff);
            }
        }
    }

    @Override
    public void place(Tile tile) {
        super.place(tile);
        double minTemp = tile.getPlanet().getMinTemperature();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile relative = tile.relative(x, y);
                relative.setTemperature(minTemp);
            }
        }
    }

}
