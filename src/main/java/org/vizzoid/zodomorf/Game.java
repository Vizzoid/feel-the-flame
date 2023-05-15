package org.vizzoid.zodomorf;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Avatar avatar;
    private final List<Planet> planets = new ArrayList<>();

    public Game() {
        avatar = new Avatar();
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public List<Planet> getPlanets() {
        return planets;
    }
}
