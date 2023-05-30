package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.DynamicRectangle;
import org.vizzoid.utils.position.Point;

public class Hitbox extends DynamicRectangle {
    public Hitbox(Point bottomLeft, double width, double height) {
        this(bottomLeft, width, height, Point.EMPTY);
    }

    public Hitbox(Point bottomLeft, double width, double height, Point velocity) {
        super(bottomLeft, width, height, velocity);
    }
}
