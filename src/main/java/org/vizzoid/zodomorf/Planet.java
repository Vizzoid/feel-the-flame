package org.vizzoid.zodomorf;

import java.util.Random;

public class Planet {

    private int time;
    private int day;
    private double temperature = buildSkyTemperature();
    private final Latice<Tile> latice = new Latice<>(500, 250);
    private final Avatar avatar;
    private final Random random = new Random();

    public Planet(Avatar avatar) {
        this.avatar = avatar;
        avatar.setPlanet(this);
        this.latice.setDefaultValue(new Tile(this, 0, 0));
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

    public double transitionTemperature(double target, double current, long ticks) {
        double change = current > target ? -0.1 : 0.1;
        // potential bug: if lagged too hard, change will be so high that it will overshoot the target temperature and may cause issues
        return temperature + (change * ticks);
    }

    public double transitionTemperature(double current, long ticks) {
        return transitionTemperature(temperature, current, ticks);
    }

    public Random getRandom() {
        return random;
    }
}
