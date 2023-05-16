package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

public class Tile implements TilePainter {

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
        public void settle() {
        }

        @Override
        public void tick(long ticks) {
        }

        @Override
        public void paint(TileInfo info) {
        }

        @Override
        public double transitionTemperature(double temperature, long ticks) {
            return temperature;
        }

        @Override
        public void swap(Tile tile) {

        }
    };


    protected final Planet planet;
    protected double temperature;
    protected final int x;
    protected final int y;
    protected Material background = Material.EMPTY;
    protected Material material = Material.EMPTY;
    protected int liquidSettleTick = 0;

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
        this.material = material;
        if (material.isSolid()) setBackground(material);
    }

    public double transitionTemperature(double temperature, long ticks) {
        return planet.transitionTemperature(this.temperature, temperature, ticks);
    }

    public void tick(long ticks) {
        if (background.isEmpty()) {
            temperature = planet.transitionTemperature(this.temperature, ticks);
        }
        if (isLiquid()) {
            if ((liquidSettleTick += ticks) > 2) {
                liquidSettleTick = 0;
                Latice<Tile> latice = planet.getTileLatice();
                Tile below = latice.get(x, y - 1);
                if (below.canMoveInto()) swap(below);
                else if (isLiquid()) {
                    Tile right = latice.get(x + 1, y);
                    if (right.isLiquid()) {
                        Tile left = latice.get(x - 1, y);
                        if (!left.isSolid()) left.setMaterial(Material.LAVA);
                    } else if (!right.isSolid()) {
                        right.setMaterial(Material.LAVA);
                    }
                }
                else {
                    Tile right = latice.get(x + 1, y);
                    if (right.canMoveInto()) swap(right);
                    else {
                        Tile left = latice.get(x - 1, y);
                        if (left.canMoveInto()) swap(left);
                    }
                }
            }
        }
    }

    public boolean isLiquid() {
        return material.isLiquid();
    }

    public boolean canMoveInto() {
        return material.isGas();
    }


    public void settle() {
        if (!isLiquid()) return;
        while (true) {
            Latice<Tile> latice = planet.getTileLatice();
            Tile below = latice.get(x, y - 1);
            if (below.getMaterial().isGas()) swap(below);
            else {
                Tile right = latice.get(x + 1, y);
                if (right.getMaterial().isGas()) swap(right);
                else {
                    Tile left = latice.get(x - 1, y);
                    if (left.getMaterial().isGas()) swap(left);
                    else return;
                }
            }
        }
    }


    public void swap(Tile tile) {
        Material material1 = material;
        material = tile.material;
        tile.material = material1;

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
        material.paint(info);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
