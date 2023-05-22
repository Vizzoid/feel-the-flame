package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.Point;

import java.awt.*;

public interface Entity {
    Point getPos();

    Image getImage();

    void tick(long ticks);
}
