package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Planet;

import java.awt.*;

public class PlanetPainter extends LaticePainter {

    private final Planet planet;
    private final IntPoint tileCenter;

    public PlanetPainter(Planet planet, DefaultEngine engine) {
        super(engine, planet.getTileLatice(), planet.getAvatar(), Images.TILE_SIZE);
        this.planet = planet;
        this.tileCenter = new IntPoint(centerStart.getX(), centerStart.getY());
    }

    @Override
    public void paint(Graphics graphics, long missedTime) {
        Avatar player = planet.getAvatar();
        double tileOffsetX = player.getTileX() - player.getCenterX();
        double tileOffsetY = player.getTileY() - player.getCenterY();
        centerStart.set(tileCenter.getX() + (tileOffsetX * squareSize), tileCenter.getY() - (tileOffsetY * squareSize));

        super.paint(graphics, missedTime);

        graphics.drawImage(Images.player(), tileCenter.getXInt() - (squareSize / 2), tileCenter.getYInt() - (squareSize), null);
    }
}
