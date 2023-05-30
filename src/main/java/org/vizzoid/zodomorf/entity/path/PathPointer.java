package org.vizzoid.zodomorf.entity.path;

import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.Point;

import java.util.ArrayList;
import java.util.List;

public class PathPointer {

    private final List<Point> waypoints;
    private final Direction[] directions;
    public final double x;
    public final double y;

    public PathPointer(double x, double y, Direction[] directions) {
        this(x, y, new ArrayList<>(), directions);
    }

    public PathPointer(double x, double y, List<Point> waypoints, Direction[] directions) {
        this.x = x;
        this.y = y;
        this.waypoints = waypoints;
        this.directions = directions;
        waypoints.add(new ImmoveablePoint(x, y));
    }

    /**
     * @return if at destination
     */
    public boolean next(PathProcess process) {
        if (process.isAtDestination(x, y)) {
            process.addPath(waypoints);
            return true;
        }

        for (Direction direction : directions) {
            double newX = x + direction.x;
            double newY = y + direction.y;

            if (process.canWalk(newX, newY)) {
                process.addPointer(new PathPointer(newX, newY, new ArrayList<>(waypoints), directions));
                process.pass(newX, newY);
            }
        }
        return false;
    }

}
