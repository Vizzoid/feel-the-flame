package org.vizzoid.zodomorf.entity.path;

public enum Direction {
    UP(0, 1),
    RIGHT(1, 0),
    DOWN(0, -1),
    LEFT(-1, 0),
    UP_RIGHT(1, 1),
    DOWN_RIGHT(1, -1),
    UP_LEFT(-1, 1),
    DOWN_LEFT(-1, -1);

    public final double x;
    public final double y;

    Direction(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double reorientX(double x) {
        return this.x + x;
    }

    public double reorientY(double y) {
        return this.y + y;
    }

}
