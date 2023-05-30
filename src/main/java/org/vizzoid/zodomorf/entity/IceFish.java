package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;

public class IceFish extends Animal {
    public IceFish(Planet planet, double x, double y) {
        super(planet, EntityType.ICE_FISH, x, y);
    }

    public IceFish(Planet planet, MoveablePoint point) {
        super(planet, EntityType.ICE_FISH, point);
    }

    @Override
    public boolean shouldHurt() {
        return super.shouldHurt() || getTile().getTemperature() > 50;
    }

}
