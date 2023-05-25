package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.engine.PlanetEngine;
import org.vizzoid.zodomorf.engine.StructurePainter;
import org.vizzoid.zodomorf.generation.PlanetGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {

    @Serial
    private static final long serialVersionUID = 6805061657282699346L;
    private static Game instance;
    private static final int UNTIL_METEOR_SHOWER = Planet.TIME_IN_DAY * 10;

    private final Avatar avatar;
    private final List<Planet> planets = new ArrayList<>();
    private StructurePainter structureBuilder = null;
    private MeteorShower meteorShower = null;
    private int untilNextMeteorShower = UNTIL_METEOR_SHOWER;

    public static Game getInstance() {
        return instance;
    }

    public StructurePainter getStructureBuilder() {
        return structureBuilder;
    }

    public void setStructureBuilder(StructurePainter structureBuilder) {
        this.structureBuilder = structureBuilder;
    }

    public Game() {
        instance = this;
        avatar = new Avatar();
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public Planet generatePlanet(PlanetGenerator generator) {
        Planet planet = generator.generate();
        planets.add(planet);
        return planet;
    }

    public void setPlanet(int planetIndex) {
        setPlanet(planets.get(planetIndex));
    }

    public void setPlanet(Planet planet) {
        planet.setAvatar(avatar);

        PlanetEngine engine = PlanetEngine.getInstance();
        if (engine != null) {
            engine.newPlanetPainter();
        }
    }

    public void tick(long ticks) {
        if (structureBuilder != null) {
            structureBuilder.tick(ticks);
            return;
        }

        for (Planet planet : planets) {
            planet.tick(ticks);
        }
        avatar.tick(ticks);

        if ((untilNextMeteorShower -= ticks) < 0) {
            if (meteorShower == null) meteorShower = new MeteorShower(planets);
            meteorShower.tick(ticks);

            if (meteorShower.isDone()) untilNextMeteorShower = UNTIL_METEOR_SHOWER;
        }
    }

}
