package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlanetPainter extends LaticePainter implements InputPainter {

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
        Tile tile = planet.getTileLatice().get(screenToX(e.getX()), screenToY(e.getY()));
        Avatar avatar = planet.getAvatar();
        avatar.setMouseTile(tile);
        avatar.setClicking(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Tile tile = planet.getTileLatice().get(screenToX(e.getX()), screenToY(e.getY()));
        Avatar avatar = planet.getAvatar();
        avatar.setMouseTile(tile);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Tile tile = planet.getTileLatice().get(screenToX(e.getX()), screenToY(e.getY()));
        Avatar avatar = planet.getAvatar();
        avatar.setMouseTile(tile);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.setMouseTile(Tile.EMPTY);
        avatar.setClicking(false);
    }

}
