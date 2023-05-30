package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.Images;

import java.awt.*;

public class MetalScorpion extends Animal {
    public MetalScorpion(Planet planet, double x, double y) {
        super(planet, EntityType.METAL_SCORPION, x, y);
    }

    public MetalScorpion(Planet planet, MoveablePoint point) {
        super(planet, EntityType.METAL_SCORPION, point);
    }

    @Override
    public Image getImage() {
        return Images.IMETAL_SCORPION;
    }
}
