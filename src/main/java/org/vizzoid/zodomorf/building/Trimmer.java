package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.TickTimer;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class Trimmer extends Building {

    private final TickTimer trimTimer = new TickTimer(this::trim, 100);

    public Trimmer(Tile tile) {
        super(tile);
    }

    public void trim() {
        iterateWidth(t -> {
            Tile below = t.below();
            if (below.getMiddleGround().isPlant()) {
                below.getPlanet().getAvatar().incrementStorage(below.getMaterial());
                below.setMiddleGround(Material.EMPTY);
            }
        });
    }

    @Override
    public void tick(long ticks) {
        trimTimer.tick(ticks);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.TRIMMER;
    }
}
