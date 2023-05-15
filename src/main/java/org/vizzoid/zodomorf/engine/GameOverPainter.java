package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.Painter;

import java.awt.*;

public class GameOverPainter implements Painter {
    @Override
    public void paint(Graphics graphics, long missedTime) {
        graphics.setColor(Color.BLACK);
        Rectangle clip = graphics.getClipBounds();
        graphics.fillRect(0, 0, clip.width, clip.height);

        int dist = clip.width / 4;
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 200));
        graphics.drawString("Game Over", dist - 50, clip.height - dist);
    }
}
