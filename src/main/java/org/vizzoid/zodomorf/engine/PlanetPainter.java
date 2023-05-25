package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.utils.position.Rectangle;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Entity;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.building.Buildable;

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
    private final ShopItem[] items;
    private final int shopX;

    private static class ShopItem {
        private final Buildable buildable;
        private final Rectangle rectangle;

        private ShopItem(Buildable buildable, Rectangle rectangle) {
            this.buildable = buildable;
            this.rectangle = rectangle;
        }
    }

    public PlanetPainter(Planet planet, DefaultEngine engine) {
        this(planet, engine, planet.getAvatar());
    }

    public PlanetPainter(Planet planet, DefaultEngine engine, LaticeCamera camera) {
        super(engine, planet.getTileLatice(), camera, Images.TILE_SIZE);
        this.planet = planet;
        this.tileCenter = new IntPoint(centerStart.getX(), centerStart.getY());

        Buildable[] array = Buildable.array();
        int width = 60;
        items = new ShopItem[array.length];
        for (int i = 0; i < array.length; i++) {
            Buildable buildable = array[i];

            int x = (int) (width * (((i % 2 * 1.35)) + 0.5));
            int y = (int) (((i / 2) + 0.5) * width);

            items[i] = new ShopItem(buildable, new Rectangle(new MoveablePoint(x, y), width, width));
        }
        this.shopX = width * 2 + width;
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
        for (ShopItem item : items) {
            MoveablePoint pos = item.rectangle.getPos();
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            graphics.drawImage(item.buildable.getImage(), x, y, null);
            graphics.setColor(Color.WHITE);
            graphics.drawString(item.buildable.getName(), x, y + Images.TILE_SIZE + 10);
            graphics.setColor(new Color(255, 255, 255, 50));
            if (item.buildable.isSame(player.getBuildable())) graphics.fillRect(x, y, (int) item.rectangle.getWidth(), (int) item.rectangle.getHeight());
            else graphics.drawRect(x, y, (int) item.rectangle.getWidth(), (int) item.rectangle.getHeight());
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
            postDraw.add(() -> graphics.drawImage(entity.getImage(), (int) ((screenX - squareSize / 2) - (tileOffsetX * squareSize)), (int) ((screenY - squareSize) - (tileOffsetY * squareSize)), null));
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
        Avatar avatar = planet.getAvatar();
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
                int x = e.getX();
                int y = e.getY();
                if (x < shopX) {
                    for (ShopItem item : items) {
                        if (item.rectangle.intersects(new ImmoveablePoint(x, y))) {
                            if (avatar.getBuildable().isSame(item.buildable)) {
                                avatar.setBuildable(Buildable.EMPTY);
                                return;
                            }
                            avatar.setBuildable(item.buildable);
                            return;
                        }
                    }
                    return;
                }
                move(e);
                avatar.setClicking(true);
            }
            case MouseEvent.BUTTON3 -> {
                move(e);
                avatar.getMouseTileRel().interact();
            }
        }

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
