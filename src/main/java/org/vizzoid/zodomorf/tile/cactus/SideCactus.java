package org.vizzoid.zodomorf.tile.cactus;

import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class SideCactus extends Cactus {
    public SideCactus(Tile tile) {
        super(tile);
    }

    @Override
    public void grow() {
        if (tile.below().getMiddleGround() != Material.CACTUS) return;
        Tile right = tile.right();
        if (!right.isSolid()) {
            right.setMiddleGround(Material.CACTUS, false);
            cactusLeg(right);
        }
        Tile left = tile.left();
        if (!left.isSolid()) {
            left.setMiddleGround(Material.CACTUS, false);
            cactusLeg(left);
        }
    }

    public void cactusLeg(Tile left) {
        left.setMiddleGroundBehavior(new CactusLeg(left));
    }

}
