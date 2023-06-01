package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Planet;

public class Cactus implements TileBehavior {

    protected Tile tile;
    protected int ticksUntilGrowth;

    public Cactus(Tile tile) {
        this.tile = tile;
        resetGrowth();
    }

    public void resetGrowth() {
        Planet planet = tile.getPlanet();
        ticksUntilGrowth = planet.getRandom().nextInt(minGrowth(), maxGrowth());
    }

    protected int minGrowth() {
        return 10000;
    }

    protected int maxGrowth() {
        return 15000;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        int cactusCount = 0;
        for (int x = -5; x < 0; x++) {
            if (tile.relative(x, 0).getMaterial() == Material.CACTUS && ++cactusCount > 4) return;
        }
        if (ticksUntilGrowth-- < 0) {
            resetGrowth();
            Tile above = tile.above();
            if (!above.isSolid()) above.setMaterial(Material.CACTUS);

            if (tile.below().getMaterial() != Material.CACTUS) return;
            Tile right = tile.right();
            if (!right.isSolid()) {
                right.setMaterial(Material.CACTUS, false);
                right.setBehavior(new SideCactus(right));
                return;
            }
            Tile left = tile.left();
            if (!left.isSolid()) {
                left.setMaterial(Material.CACTUS, false);
                right.setBehavior(new SideCactus(right));
            }
        }
    }

    @Override
    public void update() {

    }
}
