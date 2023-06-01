package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Game;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.List;

public class Rocket extends Building {

    public Rocket(Tile tile) {
        super(tile);
        for (int x = 0; x < getType().getWidth(); x++) {
            Tile relative = tile.relative(x, -1);
            if (!relative.isSolid()) relative.setMaterial(Material.FOUNDATION);
        }
    }

    @Override
    public void interact() {
        Game game = Game.getInstance();
        Avatar avatar = game.getAvatar();
        Planet planet = avatar.getPlanet();
        List<Planet> planets = game.getPlanets();
        int index = planets.indexOf(planet);
        if (++index >= planets.size()) index = 0;
        game.setPlanet(planets.get(index));
        Tile tile = avatar.getTile();
        place(tile);
    }

    @Override
    public BuildingType getType() {
        return BuildingType.ROCKET;
    }
}
