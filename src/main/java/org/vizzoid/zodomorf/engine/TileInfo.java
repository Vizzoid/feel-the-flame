package org.vizzoid.zodomorf.engine;

import java.awt.*;

/**
 * Info detailing tile, one is not created per tile, one is universally used for all, meaning that async usage without saying to their own variables may result in data of future tiles being used instead
 */
public class TileInfo {

    public Graphics graphics;
    public int screenX;
    public int screenY;
    public int squareSize;
    public long missedTime;

    public TileInfo() {

    }

    public void set(Graphics graphics, int screenX, int screenY, int squareSize, long missedTime) {
        this.graphics = graphics;
        this.screenX = screenX;
        this.screenY = screenY;
        this.squareSize = squareSize;
        this.missedTime = missedTime;
    }

    public void draw(Image image) {
        graphics.drawImage(image, screenX, screenY, null);
    }

    public void drawTile(Image image) {
        graphics.drawImage(image, screenX, screenY, squareSize, squareSize, null);
    }

    public void drawEntity(Image image) {
        graphics.drawImage(image, screenX, screenY, squareSize, squareSize + squareSize, null);
    }

}
