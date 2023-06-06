package org.vizzoid.zodomorf.tile.cactus;

import org.vizzoid.utils.random.IntDecider;
import org.vizzoid.zodomorf.TickTimer;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.StraightFallingTile;
import org.vizzoid.zodomorf.tile.Tile;

public class Cactus extends StraightFallingTile {

    protected final TickTimer growthTimer = new TickTimer(this::grow, IntDecider.between(minGrowth(), maxGrowth()));
    protected boolean canGrowLeft = true;
    protected boolean canGrowRight = true;

    public Cactus(Tile tile) {
        super(tile);
    }

    public void grow() {
        Tile above = tile.above();
        if (above.isSolid()) return;
        if (tooManyCacti()) return;
        cactus(above);

        if (tile.below().getMiddleGround() != Material.CACTUS) return;
        if (canGrowRight && tile.getPlanet().getRandom().nextBoolean()) {
            Tile right = tile.right();
            if (!right.isSolid()) {
                right.setMiddleGround(Material.CACTUS, false);
                sideCactus(right);
                for (int y = -5; y <= 5; y++) {
                    Tile relative = tile.relative(0, y);
                    if (relative.getMiddleGround() == Material.CACTUS) ((Cactus) relative.getMiddleGroundBehavior()).canGrowRight = false;
                }
                return;
            }
        }
        if (canGrowLeft) {
            Tile left = tile.left();
            if (!left.isSolid()) {
                left.setMiddleGround(Material.CACTUS, false);
                sideCactus(left);
                for (int y = -5; y <= 5; y++) {
                    Tile relative = tile.relative(0, y);
                    if (relative.getMiddleGround() == Material.CACTUS)
                        ((Cactus) relative.getMiddleGroundBehavior()).canGrowLeft = false;
                }
            }
        }
    }

    protected int minGrowth() {
        return 10000;
    }

    protected int maxGrowth() {
        return 15000;
    }

    public boolean tooManyCacti() {
        int cactusCount = 0;
        for (int y = -5; y < 0; y++) {
            if (tile.relative(0, y).getMiddleGround() == Material.CACTUS && ++cactusCount > 3) return true;
        }
        return false;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        growthTimer.tick(ticks);
    }

    public void cactus(Tile above) {
        above.setMiddleGround(Material.CACTUS);
        Cactus newCactus = (Cactus) above.getMiddleGroundBehavior();
        newCactus.canGrowRight = canGrowRight;
        newCactus.canGrowLeft = canGrowLeft;
    }

    public void sideCactus(Tile right) {
        //right.setMiddleGroundBehavior(new SideCactus(right));
    }

    @Override
    public void update() {

    }
}
