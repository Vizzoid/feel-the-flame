package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Entity;
import org.vizzoid.zodomorf.Planet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlanetPainter extends LaticePainter implements InputPainter {

    public static boolean TEMP = false;
    private final Planet planet;
    private final IntPoint tileCenter;
    private final List<Runnable> postDraw = new ArrayList<>();

    public PlanetPainter(Planet planet, DefaultEngine engine) {
        this(planet, engine, planet.getAvatar());
    }

    public PlanetPainter(Planet planet, DefaultEngine engine, LaticeCamera camera) {
        super(engine, planet.getTileLatice(), camera, Images.TILE_SIZE);
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
        for (Runnable runnable : postDraw) {
            runnable.run();
        }

        drawPlayer(graphics);
    }

    protected void drawPlayer(Graphics graphics) {
        Image playerImage = Images.IPLAYER;
        graphics.drawImage(playerImage,
                tileCenter.getXInt() - (playerImage.getWidth(null) / 2),
                tileCenter.getYInt() - (playerImage.getHeight(null) / 2) - (Images.TILE_SIZE / 2), null);
    }

    @Override
    public void paintTile(int tileX, int tileY) {
        super.paintTile(tileX, tileY);

        for (Entity entity : new ArrayList<>(planet.getEntities())) {
            if (entity == null) continue;
            Point pos = entity.getPos();
            double x = pos.getX();
            double y = pos.getY();
            if (((int) x) != tileX) continue;
            if (((int) y) != tileY) continue;

            int screenX = currentTile.screenX;
            int screenY = currentTile.screenY;
            int squareSize = currentTile.squareSize;
            Graphics graphics = currentTile.graphics;
            double tileOffsetX = ((int) x) - x;
            double tileOffsetY = ((int) y) - y;
            postDraw.add(() -> graphics.drawImage(entity.getImage(), (int) ((screenX - squareSize / 2) - (tileOffsetX * squareSize)), (int) ((screenY - squareSize) - (tileOffsetY * squareSize)), squareSize, squareSize + squareSize, null));
        }
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
        move(e);
        planet.getAvatar().setClicking(true);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        move(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        move(e);
    }

    private void move(MouseEvent e) {
        Avatar avatar = planet.getAvatar();
        avatar.getMouseTile().set(relativeX(e.getX()), relativeY(e.getY()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        move(e);
        planet.getAvatar().setClicking(false);
    }

}
