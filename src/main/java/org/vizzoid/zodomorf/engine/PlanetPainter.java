package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Planet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlanetPainter extends LaticePainter implements InputPainter {

    public static boolean TEMP = false;
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

        Image playerImage = Images.player();
        graphics.drawImage(playerImage,
                tileCenter.getXInt() - (playerImage.getWidth(null) / 2),
                tileCenter.getYInt() - (playerImage.getHeight(null) / 2) - (Images.TILE_SIZE / 2), null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Avatar player = planet.getAvatar();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                if (!player.isMovingLeft()) {
                    player.setMovingLeft(true);
                }
            }
            case KeyEvent.VK_D -> {
                if (!player.isMovingRight()) {
                    player.setMovingRight(true);
                }
            }
            case KeyEvent.VK_SPACE -> {
                if(!player.isJumping()) {
                    player.setJumping(true);
                }
            }
            case KeyEvent.VK_S -> TEMP = !TEMP;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Avatar player = planet.getAvatar();
        MoveablePoint velocity = player.getVelocity();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> {
                velocity.setX(0);
                player.setMovingLeft(false);
            }
            case KeyEvent.VK_D -> {
                velocity.setX(0);
                player.setMovingRight(false);
            }
            case KeyEvent.VK_SPACE -> {
                player.setJumping(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.getMouseTile().set(relativeX(e.getX()), relativeY(e.getY()));
        avatar.setClicking(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.getMouseTile().set(relativeX(e.getX()), relativeY(e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.getMouseTile().set(relativeX(e.getX()), relativeY(e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.setClicking(false);
    }

}
