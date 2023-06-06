package org.vizzoid.zodomorf.tile.cactus;

import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class CactusLeg extends Cactus {
    public CactusLeg(Tile tile) {
        super(tile);
    }

    @Override
    public void grow() {
        Tile above = tile.above();
        if (above.isSolid()) return;
        if (tooManyCacti()) return;

        if (!above.isSolid()) {
            above.setMiddleGround(Material.CACTUS, false);
            cactusLeg(above);
        }
    }

    public void cactusLeg(Tile left) {
        left.setMiddleGroundBehavior(new CactusLeg(left));
    }
}
