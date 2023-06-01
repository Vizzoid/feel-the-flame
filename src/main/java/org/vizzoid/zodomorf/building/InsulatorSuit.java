package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class InsulatorSuit extends Building {

    public InsulatorSuit(Tile tile) {
        super(tile);
    }

    @Override
    public void tick(long ticks) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (!equals(avatar.getSuit())) return;
        if (!avatar.spend(Material.SILICATE, 1)) avatar.setSuit(null);
    }
    
    @Override
    public void interact() {
        Avatar avatar = tile.getPlanet().getAvatar();
        avatar.setSuit(this);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.INSULATOR_SUIT;
    }

}
