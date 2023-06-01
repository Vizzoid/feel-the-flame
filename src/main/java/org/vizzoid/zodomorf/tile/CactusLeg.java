package org.vizzoid.zodomorf.tile;

public class CactusLeg extends Cactus {
    public CactusLeg(Tile tile) {
        super(tile);
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
            if (!above.isSolid()) {
                above.setMaterial(Material.CACTUS, false);
                above.setBehavior(new CactusLeg(above));
            }
        }
    }
}
