package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;

import java.awt.*;

public class Cooler extends Building {
    public Cooler() {
        super(3, 2);
    }

    @Override
    public void tick(long ticks) {
        double temperature = tile.getTemperature();
        if (temperature < tile.getPlanet().getMinTemperature()) return;
        tile.setTemperature(temperature - ticks * 5);
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD;
    }

    @Override
    public int getCost() {
        return 20;
    }

    @Override
    public String getName() {
        return "Cooler";
    }

    @Override
    public Image getImage() {
        return Images.ICOOLER;
    }
}
