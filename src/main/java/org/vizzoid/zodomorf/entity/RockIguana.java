package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;

public class RockIguana extends Animal {
    public RockIguana(Planet planet, double x, double y) {
        super(planet, EntityType.ROCK_IGUANA, x, y);
    }

    public RockIguana(Planet planet, MoveablePoint point) {
        super(planet, EntityType.ROCK_IGUANA, point);
    }

    @Override
    public boolean shouldHurt() {
        return super.shouldHurt() || getTile().getTemperature() < 250;
    }
}
