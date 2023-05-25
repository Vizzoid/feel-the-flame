package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

public class CompositeBuilding implements TileBehavior {

    private final Building building;

    public CompositeBuilding(Building building) {
        this.building = building;
    }

    @Override
    public void setTile(Tile tile) {

    }

    @Override
    public void tick(long ticks) {

    }

    @Override
    public void update() {

    }

    @Override
    public void interact() {
        building.interact();
    }
}
