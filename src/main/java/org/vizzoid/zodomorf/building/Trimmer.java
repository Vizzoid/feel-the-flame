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
        iterateWidth(this::trimTile);
    }

    public void trimTile(Tile tile) {
        Tile below = tile.below();
        if (!below.getMiddleGround().isPlant()) return;
        below.getPlanet().getAvatar().incrementStorage(below.getMaterial());
        below.setMiddleGround(Material.EMPTY);
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
