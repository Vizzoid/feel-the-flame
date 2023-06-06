package org.vizzoid.zodomorf.building.power;

import org.vizzoid.zodomorf.building.Building;
import org.vizzoid.zodomorf.building.BuildingType;
import org.vizzoid.zodomorf.tile.Tile;

public class Wire extends Building {
    public Wire(Tile tile) {
        super(tile);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.WIRE;
    }
}
