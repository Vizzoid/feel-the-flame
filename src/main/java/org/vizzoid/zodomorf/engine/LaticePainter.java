package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.engine.Painter;
import org.vizzoid.zodomorf.Latice;

import java.awt.*;

public class LaticePainter implements Painter {

    private final int squaresHalfWidth;
    private final int squaresHalfHeight;
    private final Latice<? extends TilePainter> latice;
    private final LaticeCamera camera;
    protected final int squareSize;
    protected final TileInfo currentTile = new TileInfo();
    protected final IntPoint centerStart;

    public LaticePainter(DefaultEngine engine, Latice<? extends TilePainter> latice, LaticeCamera camera, int squareSize) {
        this.latice = latice;
        this.camera = camera;
        this.squareSize = squareSize;
        Dimension dimension = engine.getCenter();
        double squaresWidthD = (dimension.width - (squareSize * 0.5)) / (double) squareSize;
        double squaresHeightD = (dimension.height - (squareSize * 0.5)) / (double) squareSize;
        int squaresWidthI = (int) squaresWidthD;
        int squaresHeightI = (int) squaresHeightD;

        this.squaresHalfWidth = squaresWidthI + (squaresWidthI != squaresWidthD ? 1 : 0);
        this.squaresHalfHeight = squaresHeightI + (squaresHeightI != squaresHeightD ? 1 : 0) + 1;

        int halfSquareSize = (int) (squareSize * 0.5);
        this.centerStart = new IntPoint(
                engine.center.width - halfSquareSize,
                engine.center.height - halfSquareSize);
    }

    @Override
    public void paint(Graphics graphics, long missedTime) {
        int cameraX = camera.getTileX();
        int cameraY = camera.getTileY();

        for (int x = -squaresHalfWidth,
             maxSquaresWidth = squaresHalfWidth + 1,
             maxSquaresHeight = squaresHalfHeight + 1;
             x <= maxSquaresWidth; x++) {
            for (int y = -squaresHalfHeight; y <= maxSquaresHeight; y++) {
                int screenX = (x * squareSize) + centerStart.getXInt();
                int screenY = (y * squareSize) + centerStart.getYInt();

                int tileX = cameraX + x;
                int tileY = cameraY - y;
                currentTile.set(graphics, screenX, screenY, squareSize, missedTime);
                paintTile(tileX, tileY);
                // graphics.drawString(tileX + ", " + tileY, screenX, screenY + squareSize);
            }
        }
    }

    public double relativeX(int screenX) {
        return ((screenX - centerStart.getXInt()) / (double) squareSize);
    }

    public double relativeY(int screenY) {
        return ((screenY - centerStart.getYInt()) / (double) -squareSize) + 1;
    }

    public double screenToX(int screenX) {
        return camera.getTileX() + relativeX(screenX);
    }

    public double screenToY(int screenY) {
        return camera.getTileY() + relativeY(screenY);
    }

    public void paintTile(int tileX, int tileY) {
        latice.get(tileX, tileY).paint(currentTile);
    }

    public TileInfo currentTile() {
        return currentTile;
    }
}
