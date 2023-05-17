package org.vizzoid.zodomorf;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 6805061657282699346L;

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
