package org.vizzoid.utils.position;

import java.io.Serial;

public class DynamicRectangle extends Rectangle {

    @Serial
    private static final long serialVersionUID = 3868192857285587301L;

    private final MoveablePoint velocity;

    public DynamicRectangle(Point bottomLeft, double width, double height, Point velocity) {
        super(bottomLeft, width, height);
        this.velocity = velocity.copyMoveable();
    }

    public Collision intersectsDynamic(Rectangle r) {
        if (velocity.isEmpty()) return Collision.NONE;

        MoveablePoint collisionPos = r.pos.copyMoveable();
        collisionPos.move(-width * 0.5, -height * 0.5);

        // this rectangle is an expanded version of the collision rectangle adding the dimensions of this rectangle,
        // this means any collision point on this rectangle will be a safe place to place this rectangle without another collision
        // (and with these two rectangles touching each other)
        Rectangle collisionRectangle = new Rectangle(collisionPos, r.width + width, r.height + height);

        MoveablePoint point = pos.copyMoveable();
        point.move(width * 0.5, height * 0.5);

        return collisionRectangle.intersectsLine(new Ray(point, velocity));
    }

    public void resolve(Rectangle r) {
        resolve(intersectsDynamic(r));
    }

    public void resolve(Collision collision) {
        if (!collision.is()) return;
        MoveablePoint point = new MoveablePoint(Math.abs(velocity.getX()), Math.abs(velocity.getY()));
        point.multiplySet(collision.getNormal());
        point.multiplySet(1 - collision.getTime());
        velocity.addSet(point);
    }

    public MoveablePoint getVelocity() {
        return velocity;
    }

    public void move(long ticks) {
        pos.addSet(velocity.multiply(ticks));
    }
}
