package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Rectangle;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.Structure;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StructurePainter extends LaticePainter implements InputPainter, LaticeCamera {

    private final Latice<Tile> latice;
    private final Planet planet;
    private final PlanetPainter planetPainter;
    private Material material = Material.FOUNDATION;
    private final ShopItem[] items = new ShopItem[Material.values().size()];
    private final List<TileRect> tileLatice = new ArrayList<>();
    private final int shopX;
    private int type = 1;
    private boolean listeningName = false;
    private String name = "";
    private Dimension dimension;
    private int cameraX;
    private int cameraY;
    private int clickSize = 3;

    @Override
    public int getTileX() {
        return cameraX;
    }

    @Override
    public int getTileY() {
        return cameraY;
    }

    private static class TileRect {
        private final int tileX;
        private final int tileY;
        private final Rectangle rectangle;

        private TileRect(int tileX, int tileY, Rectangle rectangle) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.rectangle = rectangle;
        }
    }

    private static class ShopItem {
        private final Material material;
        private final Rectangle rectangle;

        private ShopItem(Material material, Rectangle rectangle) {
            this.material = material;
            this.rectangle = rectangle;
        }
    }

    public StructurePainter(DefaultEngine engine) {
        this(engine, new Planet());
    }

    public StructurePainter(DefaultEngine engine, Planet planet) {
        super(engine, planet.getTileLatice(), null, Images.TILE_SIZE);
        this.camera = this;
        this.planet = planet;
        planet.setAvatar(new Avatar());
        this.planetPainter = new PlanetPainter(planet, engine, camera) {
            @Override
            protected void drawPlayer(Graphics graphics) {

            }
        };
        dimension = engine.getDimension();
        this.latice = planet.getTileLatice();
        cameraX = latice.getWidth() / 2;
        cameraY = latice.getHeight() / 2;
        Material[] array = Material.array();
        int width = 60;
        for (int i = 0; i < array.length; i++) {
            Material material = array[i];

            int x = (int) (width * (((i % 2 * 1.35)) + 0.5));
            int y = (int) (((i / 2) + 0.5) * width);

            items[i] = new ShopItem(material, new Rectangle(new MoveablePoint(x, y), width, width));
        }
        this.shopX = width * 2 + width;
        buildRects();
    }

    private void buildRects() {
        tileLatice.clear();
        int cameraX = camera.getTileX();
        int cameraY = camera.getTileY();

        for (int x = -squaresHalfWidth,
             maxSquaresWidth = squaresHalfWidth + 1,
             maxSquaresHeight = squaresHalfHeight + 1;
             x <= maxSquaresWidth; x++) {
            for (int y = -squaresHalfHeight; y <= maxSquaresHeight; y++) {
                int screenX = (x * squareSize) + centerStart.getXInt() - (squareSize / 2);
                int screenY = (y * squareSize) + centerStart.getYInt();
                int tileX = x + cameraX;
                int tileY = -y + cameraY;
                tileLatice.add(new TileRect(tileX, tileY, new Rectangle(new MoveablePoint(screenX, screenY), squareSize, squareSize)));
            }
        }
    }

    public Structure buildStructure() {
        int width = latice.getWidth();
        int height = latice.getHeight();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (latice.get(x, y).isSolid()) {
                    if (minX > x) {
                        minX = x;
                    }
                    if (maxX < x) {
                        maxX = x;
                    }
                    if (minY > y) {
                        minY = y;
                    }
                    if (maxY < y) {
                        maxY = y;
                    }
                }
            }
        }

        int endX = maxX - minX;
        int endY = maxY - minY;
        Structure structure = new Structure(new Latice<>(endX + 1, endY + 1));
        for (int x = 0; x <= endX; x++) {
            for (int y = 0; y <= endY; y++) {
                structure.getLatice().set(x, y, latice.get(x + minX, y + minY));
            }
        }
        return structure;
    }

    @Override
    public void paint(Graphics graphics, long missedTime) {
        for (ShopItem item : items) {
            MoveablePoint pos = item.rectangle.getPos();
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            graphics.drawImage(item.material.getImage(), x, y, null);
            graphics.setColor(Color.WHITE);
            graphics.drawString(item.material.getKey(), x, y + Images.TILE_SIZE + 10);
            graphics.setColor(new Color(255, 255, 255, 50));
            if (item.material == material) graphics.fillRect(x, y, (int) item.rectangle.getWidth(), (int) item.rectangle.getHeight());
            else graphics.drawRect(x, y, (int) item.rectangle.getWidth(), (int) item.rectangle.getHeight());
        }
        planetPainter.paint(graphics, missedTime);

        for (int x = -squaresHalfWidth,
             maxSquaresWidth = squaresHalfWidth + 1,
             maxSquaresHeight = squaresHalfHeight + 1;
             x <= maxSquaresWidth; x++) {
            for (int y = -squaresHalfHeight; y <= maxSquaresHeight; y++) {
                int screenX = (int) (((x + 0.5) * squareSize) + centerStart.getXInt());
                if (screenX < shopX) continue;
                int screenY = (y * squareSize) + centerStart.getYInt();

                graphics.setColor(new Color(255, 255, 255, 2));
                graphics.drawLine(screenX, 0, screenX, dimension.height);
                graphics.drawLine(shopX, screenY, dimension.width, screenY);
            }
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
        if (listeningName) {
            graphics.drawString(name, shopX, 100);
        }
        graphics.drawString(String.valueOf(clickSize), dimension.width - shopX, 100);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (x < shopX) {
            for (ShopItem item : items) {
                if (item.rectangle.intersects(new ImmoveablePoint(x, y))) {
                    material = item.material;
                    return;
                }
            }
            return;
        }
        click(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        click(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click(e);
    }

    private void click(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (x < shopX) return;

        for (TileRect tileRect : tileLatice) {
            if (tileRect.rectangle.intersects(new ImmoveablePoint(x, y))) {

                Consumer<Tile> setter =
                switch (type) {
                    case 1 -> t -> t.setMaterial(material, false, true);
                    case 2 -> t -> t.setMiddleGround(material, false);
                    case 3 -> t -> t.setBackground(material);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                };
                if (clickSize <= 0) {
                    setter.accept(latice.get(tileRect.tileX, tileRect.tileY));
                    return;
                }


                for (int i = tileRect.tileY - clickSize; i <= tileRect.tileY + clickSize; i++) {
                    for (int j = tileRect.tileX; Math.pow(j - tileRect.tileX, 2) + Math.pow(i - tileRect.tileY, 2) <= clickSize * clickSize; j--) {
                        setter.accept(latice.get(j, i));
                    }
                    for (int j = tileRect.tileX + 1; (j - tileRect.tileX) * (j - tileRect.tileX) + (i - tileRect.tileY) * (i - tileRect.tileY) <= clickSize * clickSize; j++) {
                        setter.accept(latice.get(j, i));
                    }
                }
            }
        }
    }

    public void tick(long ticks) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (listeningName) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                Structure structure = buildStructure();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(name + ".txt");
                    ObjectOutputStream objectOutputStream
                            = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(structure);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                listeningName = false;
                return;
            }

            char c = e.getKeyChar();
            if (Character.isLetterOrDigit(c)) name += c;
            else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) name = name.substring(0, name.length() - 1);
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                cameraY++;
                buildRects();
            }
            case KeyEvent.VK_A -> {
                cameraX--;
                buildRects();
            }
            case KeyEvent.VK_S -> {
                cameraY--;
                buildRects();
            }
            case KeyEvent.VK_D -> {
                cameraX++;
                buildRects();
            }
            case KeyEvent.VK_F -> {
                listeningName = true;
                name = "";
            }
            case KeyEvent.VK_G -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(name + ".txt");
                    ObjectInputStream objectInputStream
                            = new ObjectInputStream(fileInputStream);
                    Structure structure = (Structure) objectInputStream.readObject();
                    objectInputStream.close();

                    planet.place(structure, 100, 100);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case KeyEvent.VK_R -> latice.forEach(t -> {
                t.setMaterial(Material.EMPTY);
                t.setMiddleGround(Material.EMPTY);
                t.setBackground(Material.EMPTY);
            });
            case KeyEvent.VK_1 -> type = 1;
            case KeyEvent.VK_2 -> type = 2;
            case KeyEvent.VK_3 -> type = 3;
            case KeyEvent.VK_UP -> clickSize++;
            case KeyEvent.VK_DOWN -> clickSize--;
        }
    }
}
