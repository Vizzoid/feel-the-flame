package org.vizzoid.zodomorf.entity.path;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.entity.Entity;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerPathfinder implements Pathfinder {

    private final static ExecutorService findThread = Executors.newCachedThreadPool();
    private final static Direction[] directions = new Direction[] {Direction.RIGHT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.LEFT, Direction.UP_LEFT, Direction.DOWN_LEFT};
    private final Entity entity;
    private List<Point> path = List.of();
    private Point end = new ImmoveablePoint(0, 0);
    private int untilPath = 0;
    private boolean pathfinding = false;

    public PlayerPathfinder(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Optional<Point> getTarget() {
        return entity.getTarget();
    }

    @Override
    public Point getEnd() {
        return end;
    }

    @Override
    public boolean shouldRecalculate() {
        if (pathfinding) return false;
        if (path.isEmpty()) return true;
        Optional<Point> target = getTarget();
        if (target.isEmpty()) return false;
        return end.distanceSqr(target.getValue()) > 1;
    }

    @Override
    public void tick(long missedTime) {
        if (shouldRecalculate()) {
            if (untilPath <= 0) {
                untilPath = 20;


                Optional<Point> target = getTarget();
                if (target.isPresent()) findPathSync(target.getValue());
            }
        }
        if (untilPath > 0) untilPath -= missedTime;
        Point point = entity.getPos();
        double move = missedTime;
        Iterator<Point> it = path.iterator();

        while (move >= 0) {
            if (!it.hasNext()) {
                entity.getPos().set(end.getX(), end.getY());
                return;
            }

            Point nextPoint = it.next();
            double xDiff = nextPoint.getX() - point.getX();
            double yDiff = nextPoint.getY() - point.getY();
            double dist = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

            if (dist > move) {
                double factor = move / dist;
                entity.getPos().set((xDiff * factor) + point.getX(), (yDiff * factor) + point.getY());
                return;
            } else {
                point = nextPoint;
                it.remove();
            }
            move -= dist;
        }
    }

    @Override
    public void findPath(Point destination) {
        findThread.submit(() -> findPathSync(destination));
    }

    public void findPathSync(Point destination) {
        pathfinding = true;

        double tileX = ((int) destination.getX()) + 0.5;
        double tileY = ((int) destination.getY()) + 0.5;

        MoveablePoint start = entity.getPos();
        double entityX = ((int) start.getX()) + 0.5;
        double entityY = ((int) start.getY()) + 0.5;

        PathProcess process = new PathProcess(this, entity.getPlanet(), new ImmoveablePoint(tileX, tileY));
        process.addPointer(new PathPointer(entityX, entityY, directions));
        process.pass(entityX, entityY);
        List<Point> path = process.process();
        if (path.isEmpty()) return;
        ImmoveablePoint end = destination.immoveable();
        path.add(end);
        path.remove(0);

        this.end = end;
        this.path = path;

        pathfinding = false;
    }

    @Override
    public void setPath(List<Point> path) {
        this.path = path;
        if (path.isEmpty()) {
            end = new ImmoveablePoint(0, 0);
            return;
        }
        end = path.get(path.size() - 1);
    }

    @Override
    public List<Point> getPath() {
        return path;
    }

    public boolean canWalk(double x, double y) {
        int tileX = (int) x;
        int tileY = (int) y;

        Latice<Tile> latice = entity.getPlanet().getTileLatice();
        if (latice.get(tileX, tileY).isSolid()) return false;
        return latice.get(tileX, tileY - 1).isSolid();
    }
}
