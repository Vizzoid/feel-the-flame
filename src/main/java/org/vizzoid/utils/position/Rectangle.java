package org.vizzoid.utils.position;

import java.io.Serial;
import java.io.Serializable;

/**
 * Axis-aligned rectangle
 */
public class Rectangle implements Serializable {
    @Serial
    private static final long serialVersionUID = 3962787430742838784L;

    protected final MoveablePoint pos;
    protected final double width;
    protected final double height;

    public Rectangle(Point bottomLeft, double width, double height) {
        this.pos = bottomLeft.copyMoveable();
        this.width = width;
        this.height = height;
    }

    public MoveablePoint getPos() {
        return pos;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public boolean intersects(Point point) {
        return point.getX() >= pos.getX() && point.getY() >= pos.getY() && point.getX() < pos.getX() + width && point.getY() < pos.getY() + height;
    }

    public boolean intersects(Rectangle r) {
        return pos.getX() < r.pos.getX() + r.width && pos.getX() + width > r.pos.getX() &&
                pos.getY() < r.pos.getY() + r.height && pos.getY() + height > r.pos.getY();
    }

    public Collision intersects(Ray r) {
        MoveablePoint oppDir = new MoveablePoint(1 / r.getDirection().getX(), 1 / r.getDirection().getY());
        MoveablePoint tNear = pos.subtract(r.getStart());
        tNear.multiplySet(oppDir);

        MoveablePoint tFar = pos.copyMoveable();
        tFar.move(width, height);
        tFar.subtractSet(r.getStart());
        tFar.multiplySet(oppDir);

        if (tNear.isNaN()) return Collision.NONE;
        if (tFar.isNaN()) return Collision.NONE;

        tFar.max(tNear);

        if (tNear.getX() > tFar.getY() || tNear.getY() > tFar.getX()) return Collision.NONE;

        double tHitNear = Math.max(tNear.getX(), tNear.getY());
        double tHitFar = Math.min(tFar.getX(), tFar.getY());

        if (tHitFar < 0) return Collision.NONE;

        MoveablePoint contactPoint = r.getDirection().multiply(tHitNear);
        contactPoint.addSet(r.getStart());

        Point contactNormal = tNear.getX() == tNear.getY() ? Point.EMPTY :
                (tNear.getX() > tNear.getY() ?
                (oppDir.getX() < 0 ?
                        Point.RIGHT : Point.LEFT) :
                (oppDir.getY() < 0 ?
                        Point.UP : Point.DOWN));

        return new Collision(contactPoint, contactNormal, tHitNear);
    }

    public Collision intersectsLine(Ray line) {
        Collision collision = intersects(line);
        if (!collision.is()) return collision;
        if (collision.getTime() >= 1) return Collision.NONE;
        if (collision.getTime() < 0) return Collision.NONE;
        return collision;
    }


}
