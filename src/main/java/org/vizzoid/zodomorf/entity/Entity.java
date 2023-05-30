package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Planet;

import java.awt.*;

public interface Entity {
    Planet getPlanet();

    MoveablePoint getPos();

    Image getImage();

    void tick(long ticks);

    default Optional<Point> getTarget() {
        return Optional.empty();
    }
}
