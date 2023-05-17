package org.vizzoid.utils.position;

import java.io.Serial;
import java.io.Serializable;

/**
 * 2D position that can change without creating new position object with helper functions
 */
public class MoveablePoint implements Point, Serializable {
    
    @Serial
    private static final long serialVersionUID = -1881958513017227033L;

    private double x, y;

    public MoveablePoint() {

    }

    public MoveablePoint(double x, double y) {
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        setX(x);
        setY(y);
    }

    public void set(Point point) {
        set(point.getX(), point.getY());
    }

    public void moveX(double x) {
        setX(getX() + x);
    }

    public void moveY(double y) {
        setY(getY() + y);
    }

    public void move(double x, double y) {
        moveX(x);
        moveY(y);
    }

    @Override
    public MoveablePoint moveable() {
        return this;
    }

    @Override
    public ImmoveablePoint immoveable() {
        return new ImmoveablePoint(getX(), getY());
    }

    // movement utility (changes this object instead of creating new one

    public void addSet(Point position1) {
        setX(getX() + position1.getX());
        setY(getY() + position1.getY());
    }

    public void subtractSet(Point position1) {
        setX(getX() - position1.getX());
        setY(getY() - position1.getY());
    }

    public void multiplySet(Point position1) {
        setX(getX() * position1.getX());
        setY(getY() * position1.getY());
    }

    public void divideSet(Point position1) {
        setX(getX() / position1.getX());
        setY(getY() / position1.getY());
    }

    public void addSet(double factor) {
        setX(getX() + factor);
        setY(getY() + factor);
    }

    public void subtractSet(double factor) {
        setX(getX() - factor);
        setY(getY() - factor);
    }

    public void multiplySet(double factor) {
        setX(getX() * factor);
        setY(getY() * factor);
    }

    public void divideSet(double factor) {
        setX(getX() / factor);
        setY(getY() / factor);
    }

    /**
     * Switches the x positions of this position and the inputted one
     */
    public void swapX(MoveablePoint point) {
        double x1 = point.getX();
        point.setX(getX());
        setX(x1);
    }

    /**
     * Switches the y positions of this position and the inputted one
     */
    public void swapY(MoveablePoint point) {
        double y1 = point.getY();
        point.setY(getY());
        setY(y1);
    }

    /**
     * Switches the x and y coordinates with this object and the inputted one
     */
    public void swap(MoveablePoint point) {
        swapX(point);
        swapY(point);
    }

    /**
     * Sets THIS position to the maximum x and y of this position and the inputted one
     */
    public void max(MoveablePoint point) {
        maxX(point);
        maxY(point);
    }

    /**
     * Sets THIS position to the maximum y of this position and the inputted position
     */
    public void maxX(MoveablePoint point) {
        if (point.getX() > getX()) swapX(point);
    }

    /**
     * Sets THIS position to the maximum y of this position and the inputted position
     */
    public void maxY(MoveablePoint point) {
        if (point.getY() > getY()) swapY(point);
    }

    @Override
    public String toString() {
        return "MoveablePoint{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
    }
}
