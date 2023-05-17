package org.vizzoid.utils.position;

import java.io.Serial;
import java.io.Serializable;

/**
 * Position in 2d plane that cannot be relocated
 */
public class ImmoveablePoint implements Point, Serializable {

    @Serial
    private static final long serialVersionUID = -953012273154690741L;

    private double x, y;

    public ImmoveablePoint() {

    }

    public ImmoveablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public MoveablePoint moveable() {
        return new MoveablePoint(x, y);
    }

    @Override
    public ImmoveablePoint immoveable() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point that)) return false;

        if (Double.compare(that.getX(), x) != 0) return false;
        return Double.compare(that.getY(), y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ImmoveablePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
