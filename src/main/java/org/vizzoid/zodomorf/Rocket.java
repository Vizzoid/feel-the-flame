package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

public class Rocket extends Building {

    public Rocket(int width, int height) {
        super(width, height);
    }

    @Override
    public void place(Tile tile) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (avatar.spend(Material.NICKEL, 30)) return;
        super.place(tile);
    }
}
