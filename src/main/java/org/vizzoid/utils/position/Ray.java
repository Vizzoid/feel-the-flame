package org.vizzoid.utils.position;

public class Ray {

    private final MoveablePoint start;
    private final MoveablePoint direction;

    public Ray(Point start, Point direction) {
        this.start = start.copyMoveable();
        this.direction = direction.copyMoveable();
    }

    public MoveablePoint getStart() {
        return start;
    }

    public MoveablePoint getDirection() {
        return direction;
    }

    public Collision intersects(Rectangle rectangle) {
        return rectangle.intersects(this);
    }

}
