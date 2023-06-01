package org.vizzoid.zodomorf.tile;

public class SideCactus extends Cactus {
    public SideCactus(Tile tile) {
        super(tile);
    }

    @Override
    public void tick(long ticks) {
        if (ticksUntilGrowth-- < 0) {
            resetGrowth();

            if (tile.below().getMaterial() != Material.CACTUS) return;
            Tile right = tile.right();
            if (!right.isSolid()) {
                right.setMaterial(Material.CACTUS, false);
                right.setBehavior(new CactusLeg(right));
                return;
            }
            Tile left = tile.left();
            if (!left.isSolid()) {
                left.setMaterial(Material.CACTUS, false);
                left.setBehavior(new CactusLeg(left));
            }
        }
    }
}
