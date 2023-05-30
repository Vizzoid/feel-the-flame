package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;

public class CactusBear extends Animal {

    public CactusBear(Planet planet, MoveablePoint point) {
        super(planet, EntityType.CACTUS_BEAR, point);
    }

    public CactusBear(Planet planet, double x, double y) {
        super(planet, EntityType.CACTUS_BEAR, x, y);
    }
}
