package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.BoundaryTile;
import org.vizzoid.zodomorf.tile.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Planet implements Serializable {

    @Serial
    private static final long serialVersionUID = -7424585672793399059L;

    //<editor-fold desc="Temperature Lookup Table">
    private static final int TEMP_LOOKUP_TABLE_SIZE = 2000;
    private static final double[] TEMP_LOOKUP_TABLE = new double[TEMP_LOOKUP_TABLE_SIZE]; // precalculated temperature transfer table

    private static double buildTransition(double diff) {
        return 25 / (633 * Math.pow(0.979, diff) - 50);
    }

    static {
        for (int i = 0; i < TEMP_LOOKUP_TABLE_SIZE; i++) {
            TEMP_LOOKUP_TABLE[i] = buildTransition(i * 0.1);
        }
    }

    public static double getNewTemperature(double target, double current) {
        if (target == current) return current;
        boolean aboveTarget = target < current;
        double temperatureChange = getTemperatureChange(Math.abs(target - current));
        if (aboveTarget) temperatureChange *= -1;
        return current + temperatureChange;
    }

    public static double getTemperatureChange(double diff) {
        return TEMP_LOOKUP_TABLE[Math.max(0, Math.min(TEMP_LOOKUP_TABLE_SIZE - 1, (int) (diff * 10)))];
    }
    //</editor-fold>

    @Serial
    private void readObject(ObjectInputStream ois)
            throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        for (Tile tile : latice) {
            tile.setPlanet(this);
        }
    }

    private int time = 0;
    private int day;
    private transient double temperature = buildSkyTemperature();
    private final Latice<Tile> latice = new Latice<>(500, 250);
    private transient Avatar avatar;
    private final Random random = new Random();

    public Planet() {
        this.latice.setDefaultValue(new Tile(this, 0, 0));
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
        avatar.setPlanet(this);
    }

    public int getDay() {
        return day;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public boolean isDaytime() {
        return time < 20000;
    }

    public boolean isNighttime() {
        return time >= 20000;
    }

    public double getTemperature() {
        return temperature;
    }

    public Latice<Tile> getTileLatice() {
        return latice;
    }

    private double buildSkyTemperature() {
        if (isDaytime()) {
            double x = (time - 12566);

            return ((x * x) / -16000000) + 100;
        }
        double x = (time - 25620.5);

        return ((x * x) / 190000) - 100;
    }

    public void tick(long ticks) {
        avatar.tick(ticks);

        if ((time += ticks) > 30000) {
            time = 0;
            day++;
        }
        temperature = buildSkyTemperature();
        for (Tile tile : latice) {
            tile.tick(ticks);
        }
    }

    public void transitionTemperature(Tile targetTile, Tile currentTile) {
        if (targetTile instanceof BoundaryTile) return;

        double target = targetTile.getTemperature();
        double current = currentTile.getTemperature();

        if (target == current) return;
        boolean aboveTarget = target < current;
        double temperatureChange = getTemperatureChange(Math.abs(target - current));
        if (aboveTarget) temperatureChange *= -1;
        temperatureChange *= 80 - (currentTile.getHealth() * 4);
        currentTile.setTemperature(current + temperatureChange);
        targetTile.setTemperature(target - temperatureChange);
    }

    public void transitionTemperature(Tile currentTile) {
        double current = currentTile.getTemperature();

        if (temperature == current) return;
        boolean aboveTarget = temperature < current;
        double temperatureChange = getTemperatureChange(Math.abs(temperature - current));
        if (aboveTarget) temperatureChange *= -1;
        currentTile.setTemperature(current + temperatureChange);
    }

    public Random getRandom() {
        return random;
    }
}
