package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.PlanetPainter;
import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

import java.awt.*;
import java.io.*;

public class Tile implements TilePainter, Serializable {

    @Serial
    private static final long serialVersionUID = 8582575836241128646L;
    public static final Tile EMPTY = new Tile(0, 0) {
        @Override
        public void setBackground(Material background) {
        }

        @Override
        public void setMaterial(Material material) {
        }

        @Override
        public void setTemperature(double temperature) {
        }

        @Override
        public void tick(long ticks) {
        }

        @Override
        public void paint(TileInfo info) {
        }

        @Override
        public void swap(Tile tile) {

        }
    };

    protected transient Planet planet;
    protected double temperature;
    protected final int x;
    protected final int y;
    protected transient Material background = Material.EMPTY;
    protected transient Material material = Material.EMPTY;
    protected transient TileBehavior behavior = TileBehavior.EMPTY;

    @Serial
    private void writeObject(ObjectOutputStream oos)
      throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(background.getKey());
        oos.writeUTF(material.getKey());
    }

    @Serial
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        background = Material.fromKey(ois.readUTF());
        material = Material.fromKey(ois.readUTF());
        behavior = material.buildBehavior(this);
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    private Tile(int x, int y) {
        this.planet = null;
        this.temperature = 0;
        this.x = x;
        this.y = y;
    }


    public Tile(Planet planet, int x, int y) {
        this.planet = planet;
        this.temperature = planet.getTemperature();
        this.x = x;
        this.y = y;
    }

    public Tile(Planet planet, Material material, int x, int y) {
        this(planet, x, y);
        setMaterial(material);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public Material getBackground() {
        return this.background;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setBackground(Material background) {
        this.background = background;
    }

    public void setMaterial(Material material) {
        setMaterial(material, true);
    }

    public void setMaterial(Material material, boolean updateBehavior) {
        this.material = material;
        if (material.isSolid()) setBackground(material);
        if (updateBehavior) behavior = material.buildBehavior(this);
        updateNeighbors();
    }

    public void tick(long ticks) {
        if (background.isEmpty()) {
            planet.transitionTemperature(this);
        }
        Latice<Tile> latice = planet.getTileLatice();
        //planet.transitionTemperature(latice.get(x + 1, y), this);
        planet.transitionTemperature(latice.get(x - 1, y), this);
        //planet.transitionTemperature(latice.get(x, y + 1), this);
        planet.transitionTemperature(latice.get(x, y - 1), this);

        behavior.tick(ticks);
    }

    public boolean isLiquid() {
        return material.isLiquid();
    }

    public boolean canMoveInto() {
        return material.isGas();
    }

    public void update() {
        behavior.update();
    }

    public void updateNeighbors() {
        Latice<Tile> latice = planet.getTileLatice();
        latice.get(x + 1, y).update();
        latice.get(x - 1, y).update();
        latice.get(x, y + 1).update();
        latice.get(x, y - 1).update();
    }

    public void swap(Tile tile) {
        Material material1 = material;
        setMaterial(tile.material, false);
        tile.setMaterial(material1, false);

        TileBehavior behavior1 = behavior;
        behavior = tile.behavior;
        tile.behavior = behavior1;

        behavior.setTile(tile);
        tile.behavior.setTile(tile);

        double temperature1 = temperature;
        temperature = tile.temperature;
        tile.temperature = temperature1;
    }

    public boolean isSolid() {
        return material.isSolid();
    }

    private static final Color TILE_BREAKING = new Color(255, 255, 255, 118);

    @Override
    public void paint(TileInfo info) {
        //background.paintBackground(info);
        material.paint(info);

        Avatar avatar = planet.getAvatar();
        if (isSolid() && equals(avatar.getClickTile())) {
            double miningTileHealth = avatar.getMiningTileHealth();
            double health = getHealth();
            info.graphics.setColor(TILE_BREAKING);
            int height = (int) (info.squareSize * ((health - miningTileHealth) / health));
            info.graphics.fillRect(info.screenX, info.screenY + (info.squareSize - height), info.squareSize, height);
        }

        if (!PlanetPainter.TEMP) return;
        Color color = new Color(
                temperature > 0 ? 255 : Math.min(255, Math.max(0, (int) (temperature * 51 / 20) + 255)), 0,
                temperature < 0 ? 255 : Math.min(255, Math.max(0, (int) (temperature * 51 / -20) + 255)), 255);
        info.graphics.setColor(color);
        info.graphics.fillRect(info.screenX, info.screenY, info.squareSize, info.squareSize);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return material.getHealth();
    }

    public Planet getPlanet() {
        return planet;
    }

    public TileBehavior getBehavior() {
        return behavior;
    }

}
