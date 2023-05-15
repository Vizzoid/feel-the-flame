package org.vizzoid.utils.position;

public class Collision {

    public static final Collision NONE = new Collision(null, null, 0) {
        @Override
        public boolean is() {
            return false;
        }

        @Override
        public double getTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Point getHitPoint() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Point getNormal() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "no collision";
        }
    };

    private final Point hitPoint;
    private final Point normal;
    private final double time;

    public Collision(Point hitPoint, Point normal, double time) {
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.time = time;
    }

    /**
     * @return if collision actually occurred
     */
    public boolean is() {
        return true;
    }

    public double getTime() {
        return time;
    }

    public Point getHitPoint() {
        return hitPoint;
    }

    public Point getNormal() {
        return normal;
    }

}
