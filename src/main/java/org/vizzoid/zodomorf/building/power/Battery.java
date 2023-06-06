package org.vizzoid.zodomorf.building.power;

import org.vizzoid.zodomorf.building.Building;
import org.vizzoid.zodomorf.building.BuildingType;
import org.vizzoid.zodomorf.tile.Tile;

public class Battery extends Building {

    public static final int MAX_WATTS = 15000;
    private int watts;

    public Battery(Tile tile) {
        super(tile);
    }

    @Override
    public void tick(long ticks) {
        super.tick(ticks);

        if (watts > 0) watts--;
    }

    public void use(int watts) {
        fuel(-watts);
    }

    public void fuel() {
        watts++;
    }

    public void fuel(int watts) {
        this.watts += watts;

        if (this.watts > MAX_WATTS) this.watts = MAX_WATTS;
    }

    @Override
    public BuildingType getType() {
        return BuildingType.BATTERY;
    }
}
