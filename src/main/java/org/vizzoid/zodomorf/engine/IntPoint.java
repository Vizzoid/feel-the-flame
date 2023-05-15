package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.position.MoveablePoint;

public class IntPoint extends MoveablePoint {

    public IntPoint() {
    }

    public IntPoint(double x, double y) {
        super(x, y);
    }

    public int getXInt() {
        return (int) getX();
    }

    public int getYInt() {
        return (int) getY();
    }

}
