package org.vizzoid.zodomorf.entity.path;

import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Planet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathProcess {

    private final Pathfinder pathfinder;
    private final Planet planet;
    private final Point destination;
    private final Set<Point> passedTiles = new HashSet<>();
    private final List<PathPointer> pointers = new ArrayList<>();
    private List<Point> path = List.of();

    public PathProcess(Pathfinder pathfinder, Planet planet, Point destination) {
        this.pathfinder = pathfinder;
        this.planet = planet;
        this.destination = destination;
    }

    private List<PathPointer> getPointers() {
        return pointers;
    }

    public void addPointer(PathPointer pointer) {
        pointers.add(pointer);
    }

    public void addPath(List<Point> path) {
        this.path = path;
    }

    public void pass(double x, double y) {
        passedTiles.add(new ImmoveablePoint((int) x, (int) y));
    }

    public boolean canWalk(double x, double y) {
        if (!pathfinder.canWalk(x, y)) return false;
        int tileX = (int) x;
        int tileY = (int) y;
        return !passedTiles.contains(new ImmoveablePoint(tileX, tileY));
    }

    public boolean next() {
        ArrayList<PathPointer> pointers = new ArrayList<>(this.pointers);
        this.pointers.clear();
        for (PathPointer pointer : pointers) {
            if (pointer.next(this)) return true;
        }
        return false;
    }

    public List<Point> process() {
        while (!pointers.isEmpty()) {
            if (next()) break;
        }
        return path;
    }

    public Point getDestination() {
        return destination;
    }

    public boolean isAtDestination(double x, double y) {
        return destination.getX() == x &&
                destination.getY() == y;
    }
}
