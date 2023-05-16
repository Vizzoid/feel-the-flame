package org.vizzoid.zodomorf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

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
