package org.vizzoid.zodomorf.entity.path;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.Point;

import java.util.List;

public interface Pathfinder {

    Pathfinder NONE = new Pathfinder() {
        @Override
        public Optional<Point> getTarget() {
            return Optional.empty();
        }

        @Override
        public Point getEnd() {
            return Point.EMPTY;
        }

        @Override
        public boolean shouldRecalculate() {
            return false;
        }

        @Override
        public void tick(long missedTime) {

        }

        @Override
        public void findPath(Point destination) {

        }

        @Override
        public void setPath(List<Point> path) {

        }

        @Override
        public List<Point> getPath() {
            return List.of();
        }

        @Override
        public boolean canWalk(double x, double y) {
            return false;
        }
    };

    Optional<Point> getTarget();

    Point getEnd();

    boolean shouldRecalculate();

    void tick(long missedTime);

    void findPath(Point destination);

    void setPath(List<Point> path);

    List<Point> getPath();

    boolean canWalk(double x, double y);
}
