package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.BoundaryTile;
import org.vizzoid.zodomorf.tile.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet implements Serializable {

    public static final int TIME_IN_DAY = 15000;
    public static final double DAY_TEMP = (TIME_IN_DAY * 0.5) / Math.PI;
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

    private int time = 1000;
    private int day;
    private transient double temperature;
    private final PlanetLatice latice = new PlanetLatice(this);
    private transient Avatar avatar;
    private final Random random = new Random();
    private final List<Entity> entities = new ArrayList<>();
    private int minTemperature = 10;
    private int maxTemperature = 210;

    public Planet() {
        temperature = buildSkyTemperature();
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
        avatar.setPlanet(this);

        int x = (int) (latice.getWidth() * 0.5);
        avatar.getPos().set(x, getHighestY(x) + 1);
    }

    public int getDay() {
        return day;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public double getTemperature() {
        return temperature;
    }

    public Latice<Tile> getTileLatice() {
        return latice;
    }

    private double buildSkyTemperature() {
        int middle = (minTemperature + maxTemperature) / 2;

        return middle * Math.sin(time / DAY_TEMP) + middle;
    }

    public void newDay() {
        day++;
    }

    public void tick(long ticks) {
        if ((time += ticks) > TIME_IN_DAY) {
            time = 0;
            newDay();
        }
        temperature = buildSkyTemperature();
        for (Tile tile : latice) {
            tile.tick(ticks);
        }
        for (Entity entity : new ArrayList<>(entities)) {
            entity.tick(ticks);
        }
    }

    public void transitionTemperature(Tile targetTile, Tile currentTile) {
        if (targetTile instanceof BoundaryTile) return;
        if (targetTile == null) return;

        double target = targetTile.getTemperature();
        double current = currentTile.getTemperature();

        if (target == current) return;
        boolean aboveTarget = target < current;
        double temperatureChange = getTemperatureChange(Math.abs(target - current));
        if (aboveTarget) temperatureChange *= -1;
        temperatureChange *= 20 + ((Math.max(0, 8 - currentTile.getHealth() - targetTile.getHealth()) * 20));
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

    public int randomX() {
        return random.nextInt(latice.getWidth());
    }

    public int randomY() {
        return random.nextInt(latice.getHeight());
    }

    public int getHighestY(int x) {
        int height = latice.getHeight();
        for (int y = height - 2; y >= 0; y--) {
            if (latice.get(x, y).isSolid()) return y;
        }
        return 0;
    }

    public int getWidth() {
        return latice.getWidth();
    }

    public int getHeight() {
        return latice.getHeight();
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void place(Structure structure) {
        place(structure, 0, 0);
    }

    public void place(Structure structure, int modX, int modY) {
        Latice<Tile> structureLatice = structure.getLatice();
        int width = structureLatice.getWidth();
        int height = structureLatice.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = structureLatice.get(x, y);
                if (tile == null) continue;
                Tile tile1 = latice.get(x + modX, y + modY);
                tile1.setMaterial(tile.getMaterial());
                tile1.setMiddleGround(tile.getMiddleGround());
                tile1.setBackground(tile.getBackground());
            }
        }
    }

    public void resetTemperature() {
        setTemperature(buildSkyTemperature());
    }
}
