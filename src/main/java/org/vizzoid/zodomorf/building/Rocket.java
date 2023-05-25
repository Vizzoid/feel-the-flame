package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Game;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;
import java.util.List;

public class Rocket extends Building {

    public Rocket() {
        super(3, 8);
    }

    @Override
    public Material getMaterial() {
        return Material.NICKEL;
    }

    @Override
    public int getCost() {
        return 30;
    }

    @Override
    public String getName() {
        return "Rocket";
    }

    @Override
    public Image getImage() {
        return Images.IROCKET;
    }

    @Override
    public void place(Tile tile) {
        super.place(tile);
        for (int x = 0; x < width; x++) {
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
    public Building clone() {
        return super.clone();
    }
}
