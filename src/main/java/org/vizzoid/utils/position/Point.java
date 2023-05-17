package org.vizzoid.utils.position;

import com.google.errorprone.annotations.CheckReturnValue;

/**
 * 2D immutable position template
 */
public interface Point {

    Point UP = new ImmoveablePoint(0, 1);
    Point DOWN = new ImmoveablePoint(0, -1);
    Point RIGHT = new ImmoveablePoint(1, 0);
    Point LEFT = new ImmoveablePoint(-1, 0);
    Point EMPTY = new ImmoveablePoint(0, 0);

    double getX();

    double getY();

    MoveablePoint moveable();

    ImmoveablePoint immoveable();

    /**
     * Creates new point with the same coordinates but moveable
     */
    @CheckReturnValue
    default MoveablePoint copyMoveable() {
        return new MoveablePoint(getX(), getY());
    }

    /**
     * Creates new point with the same coordinates but immoveable
     */
    @CheckReturnValue
    default ImmoveablePoint copyImmoveable() {
        return new ImmoveablePoint(getX(), getY());
    }

    /**
     * new point that is a vector from this position to the inputted position
     * same as position.subtract(this)
     */
    default MoveablePoint line(Point position) {
        MoveablePoint line = new MoveablePoint();
        line.setX(position.getX() - getX());
        line.setY(position.getY() - getY());

        return line;
    }

    /**
     * normalizes the vector (reduces/increases the vector to a length of 1
     * same as divide(length())
     */
    default MoveablePoint normalize() {
        MoveablePoint position = moveable();
        double length = length();

        position.setX(position.getX() / length);
        position.setY(position.getY() / length);

        return position;
    }

    /**
     * pythagorean's scale, length of vector
     */
    default double lengthSqr() {
        double x = getX();
        double y = getY();

        return (x * x + y * y);
    }

    /**
     * pythagorean's scale, length of vector
     */
    default double length() {
        return Math.sqrt(lengthSqr());
    }

    default double distanceSqr(Point point) {
        double xDiff = point.getX() - getX();
        double yDiff = point.getY() - getY();
        return (xDiff * xDiff) + (yDiff * yDiff);
    }

    default double distance(Point point) {
        return Math.sqrt(distanceSqr(point));
    }

    default double dotProduct(Point position) {
        return getX() * position.getX() +
                getY() * position.getY();
    }

    // movement utility

    /**
     * Adds the values of position1 to this position and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint add(Point position1) {
        MoveablePoint position = copyMoveable();
        position.addSet(position1);
        return position;
    }

    /**
     * Subtracts the values of this position by position1 and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint subtract(Point position1) {
        MoveablePoint position = copyMoveable();
        position.subtractSet(position1);
        return position;
    }

    /**
     * Multiplies the values of this position by position1 and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint multiply(Point position1) {
        MoveablePoint position = copyMoveable();
        position.multiplySet(position1);
        return position;
    }

    /**
     * Divides the values of this position by position1 and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint divide(Point position1) {
        MoveablePoint position = copyMoveable();
        position.divideSet(position1);
        return position;
    }

    /**
     * Adds the factor to each coord of this position and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint add(double factor) {
        MoveablePoint position = copyMoveable();
        position.addSet(factor);
        return position;
    }

    /**
     * Subtracts the factor to each coord of this position and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint subtract(double factor) {
        MoveablePoint position = copyMoveable();
        position.subtractSet(factor);
        return position;
    }

    /**
     * Multiplies the factor to each coord of this position and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint multiply(double factor) {
        MoveablePoint position = copyMoveable();
        position.multiplySet(factor);
        return position;
    }

    /**
     * Divides the factor to each coord of this position and stores it in a NEW position
     */
    @CheckReturnValue
    default MoveablePoint divide(double factor) {
        MoveablePoint position = copyMoveable();
        position.divideSet(factor);
        return position;
    }

    default boolean intersects(Rectangle rectangle) {
        return rectangle.intersects(this);
    }

    default boolean isNaN() {
        return Double.isNaN(getX()) || Double.isNaN(getY());
    }

    default boolean isEmpty() {
        return getX() == 0 && getY() == 0;
    }

}
