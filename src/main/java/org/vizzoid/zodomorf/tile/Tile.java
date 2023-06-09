package org.vizzoid.zodomorf.tile;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.Point;
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
    private static final Color TILE_BREAKING = new Color(255, 255, 255, 118);

    protected transient Planet planet;
    protected double temperature;
    protected final ImmoveablePoint pos;
    protected transient Material background = Material.EMPTY;
    protected transient Material middleGround = Material.EMPTY;
    protected transient Material material = Material.EMPTY;
    protected transient TileBehavior behavior = TileBehavior.EMPTY;
    protected transient TileBehavior middleGroundBehavior = TileBehavior.EMPTY;

    private Tile(int x, int y) {
        this.pos = new ImmoveablePoint(x, y);
    }

    @Serial
    private void writeObject(ObjectOutputStream oos)
      throws IOException {
        oos.defaultWriteObject();
        oos.writeUTF(background.getKey());
        oos.writeUTF(middleGround.getKey());
        oos.writeUTF(material.getKey());
    }

    @Serial
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        background = Material.fromKey(ois.readUTF());
        middleGround = Material.fromKey(ois.readUTF());
        material = Material.fromKey(ois.readUTF());
        behavior = material.buildBehavior(this);
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
        this.temperature = planet.getTemperature();
    }


    public Tile(Planet planet, int x, int y) {
        setPlanet(planet);
        this.pos = new ImmoveablePoint(x, y);
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

    public Material getMiddleGround() {
        return middleGround;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setBackground(Material background) {
        this.background = background;
    }

    public void setMiddleGround(Material middleGround) {
        setMiddleGround(middleGround, true);
    }

    public void setMiddleGround(Material middleGround, boolean updateBehavior) {
        this.middleGround = middleGround;
        if (updateBehavior) middleGroundBehavior = middleGround.buildBehavior(this);
    }

    public void setMaterial(Material material) {
        setMaterial(material, true);
    }

    public void setMaterial(Material material, boolean updateBehavior) {
        setMaterial(material, updateBehavior, true);
    }

    public void setMaterial(Material material, boolean updateBehavior, boolean updateBackground) {
        this.material = material;
        if (updateBackground && material.isSolid()) setBackground(material);
        if (updateBehavior) behavior = material.buildBehavior(this);
        // updateNeighbors();
    }

    public void tick(long ticks) {
        if (background.isEmpty()) {
            planet.transitionTemperature(this);
        }
        if (!material.isEmpty() || !middleGround.isEmpty() || !background.isEmpty()) {
            //planet.transitionTemperature(latice.get(x + 1, y), this);
            Tile left = left();
            transitionTemperature(left);
            //planet.transitionTemperature(latice.get(x, y + 1), this);
            Tile below = below();
            transitionTemperature(below);
        } else {
            temperature = planet.getTemperature();
        }

        behavior.tick(ticks);
        middleGroundBehavior.tick(ticks);
    }

    public void transitionTemperature(Tile tile) {
        if (tile.isBoundary()) return;
        double tileTemp = tile.getTemperature();

        if (Math.abs(tileTemp - temperature) > 100) {
            double midTemp = (tileTemp + temperature) / 2;
            setTemperature(midTemp);
            tile.setTemperature(midTemp);
        }
        else {
            planet.transitionTemperature(tile, this);
        }
    }

    public TileBehavior getMiddleGroundBehavior() {
        return middleGroundBehavior;
    }

    public void setMiddleGroundBehavior(TileBehavior middleGroundBehavior) {
        this.middleGroundBehavior = middleGroundBehavior;
        middleGroundBehavior.setTile(this);
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
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        latice.get(x + 1, y).update();
        latice.get(x - 1, y).update();
        latice.get(x, y + 1).update();
        latice.get(x, y - 1).update();
    }

    public void swap(Tile tile) {
        Material material1 = material;
        setMaterial(tile.material, false, false);
        tile.setMaterial(material1, false, false);

        TileBehavior behavior1 = behavior;
        behavior = tile.behavior;
        tile.behavior = behavior1;

        behavior.setTile(this);
        tile.behavior.setTile(tile);

        double temperature1 = temperature;
        temperature = tile.temperature;
        tile.temperature = temperature1;
    }

    public boolean isSolid() {
        return material.isSolid();
    }

    @Override
    public void paint(TileInfo info) {
        background.paintBackground(info);
        material.paint(info, behavior);
        middleGround.paint(info, middleGroundBehavior);

        Avatar avatar = planet.getAvatar();
        Material material = this.material.isSolid() ? this.material : middleGround;
        if (material.isSolid() && equals(avatar.getClickTile())) {
            double miningTileHealth = avatar.getMiningTileHealth();
            double health = material.getHealth();
            info.graphics.setColor(TILE_BREAKING);
            int height = (int) (info.squareSize * ((health - miningTileHealth) / health));
            info.graphics.fillRect(info.screenX, info.screenY + (info.squareSize - height), info.squareSize, height);
        }

        if (!PlanetPainter.TEMP) return;
        Color color = new Color(
                temperature > 110 ? 255 : Math.min(255, Math.max(0, (int) ((temperature - 110) * 51 / 20) + 255)), 0,
                temperature < 110 ? 255 : Math.min(255, Math.max(0, (int) ((temperature - 110) * 51 / -20) + 255)), 255);
        info.graphics.setColor(color);
        info.graphics.fillRect(info.screenX, info.screenY, info.squareSize, info.squareSize);

        Color color1 = new Color(
                77, 0,
                255, 255);
        info.graphics.setColor(color1);
        info.graphics.fillRect(0, 0, 100, 100);
    }

    public int getX() {
        return (int) pos.getX();
    }

    public int getY() {
        return (int) pos.getY();
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

    public void setBehavior(TileBehavior behavior) {
        this.behavior = behavior;
        behavior.setTile(this);
    }

    public boolean isBoundary() {
        return false;
    }

    public Tile relative(int x, int y) {
        if (planet == null) {
            throw new NullPointerException(pos.toString() + ", " + getClass().getName());
        }
        return planet.getTileLatice().get(pos.getX() + x, pos.getY() + y);
    }

    public Tile above() {
        return relative(0, 1);
    }

    public Tile below() {
        return relative(0, -1);
    }

    public Tile left() {
        return relative(-1, 0);
    }

    public Tile right() {
        return relative(1, 0);
    }

    public Optional<Tile> freeAdjacent() {
        {
            Tile above = above();
            if (!above.isSolid()) return Optional.of(above);
        }
        {
            Tile right = right();
            if (!right.isSolid()) return Optional.of(right);
        }
        {
            Tile left = left();
            if (!left.isSolid()) return Optional.of(left);
        }
        {
            Tile below = below();
            if (!below.isSolid()) return Optional.of(below);
        }
        return Optional.empty();
    }

    public Point getPos() {
        return pos;
    }

    public void interact() {
        behavior.interact();
        middleGroundBehavior.interact();
    }
}
