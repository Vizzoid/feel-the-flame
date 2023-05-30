package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;

public class ForestDeer extends Animal {
    public ForestDeer(Planet planet, double x, double y) {
        super(planet, EntityType.FOREST_DEER, x, y);
    }

    public ForestDeer(Planet planet, MoveablePoint point) {
        super(planet, EntityType.FOREST_DEER, point);
    }
}
