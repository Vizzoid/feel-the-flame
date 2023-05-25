package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.Game;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;

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
    public void interact() {
        Game game = Game.getInstance();
        Planet planet = game.getAvatar().getPlanet();
        List<Planet> planets = game.getPlanets();
        int index = planets.indexOf(planet);
        if (++index >= planets.size()) index = 0;
        game.setPlanet(planets.get(index));
    }

    @Override
    public Building clone() {
        return super.clone();
    }
}
