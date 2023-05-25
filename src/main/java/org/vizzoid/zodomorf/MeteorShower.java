package org.vizzoid.zodomorf;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MeteorShower implements Serializable {

    @Serial
    private static final long serialVersionUID = -136890939556198069L;

    private final List<Planet> planets;
    private int untilStop = Planet.TIME_IN_DAY;

    public MeteorShower(List<Planet> planets) {
        this.planets = planets;
    }

    public void tick(long ticks) {
        untilStop -= ticks;
        for (Planet planet : planets) {
            planet.addEntity(new Meteor(planet));
        }
    }

    public boolean isDone() {
        return untilStop < 0;
    }

}
