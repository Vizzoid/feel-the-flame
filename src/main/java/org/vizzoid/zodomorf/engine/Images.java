package org.vizzoid.zodomorf.engine;

import org.vizzoid.zodomorf.Avatar;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Images {

    public static final Image IPLAYER, IFOUNDATION, ICOPPER_ORE, ISEDIMENTARY_ROCK, IIGNEOUS_ROCK, IDIRT, ILAVA, ISHADOW,
            IDEBRIS, ICLAY, ISILICATE, INICKEL, IASH, IGRAVEL, IGOLD, ISAND, ISANDSTONE, IICE, IOBSIDIAN, IWATER, ISULFUR, IMERCURY;

    public static final int TILE_SIZE = 3;

    static  {
        IPLAYER = ofColor(Color.RED, (int) (Avatar.HITBOX_WIDTH * TILE_SIZE), (int) (Avatar.HITBOX_HEIGHT * TILE_SIZE));
        IFOUNDATION = ofColor(Color.LIGHT_GRAY);
        ICOPPER_ORE = ofColor(new Color(205, 127, 50));
        ISEDIMENTARY_ROCK = ofColor(Color.GRAY);
        IIGNEOUS_ROCK = ofColor(Color.DARK_GRAY);
        IDIRT = ofColor(new Color(98, 68, 51));
        ILAVA = ofColor(Color.ORANGE);
        ISHADOW = ofColor(Color.BLACK);
        IDEBRIS = ofColor(Color.GRAY);
        ICLAY = ofColor(new Color(138, 124, 85));
        ISILICATE = ofColor(new Color(156,187,120));
        INICKEL = ofColor(new Color(201,186,169));
        IASH = ofColor(Color.LIGHT_GRAY);
        IGRAVEL = ofColor(Color.GRAY);
        IGOLD = ofColor(new Color(255, 215, 0));
        ISAND = ofColor(new Color(194, 178, 128));
        ISANDSTONE = ofColor(new Color(213,194,165));
        IICE = ofColor(Color.CYAN);
        IOBSIDIAN = ofColor(new Color(113,98,122));
        IWATER = ofColor(Color.BLUE);
        ISULFUR = ofColor(new Color(243, 222, 56));
        IMERCURY = ofColor(new Color(219,206,202));
    }

    private Images() {

    }

    public static Image player() {
        return IPLAYER;
    }

    public static String getJarPath() {
        try {
            return new File(Images.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String appendExtension(String filename) {
        if (!filename.contains(".")) filename += ".png";
        return filename;
    }

    public static BufferedImage getTexture(String name) {
        try {
            return ImageIO.read(new File(getJarPath() + "\\texture\\" + appendExtension(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getResource(String name) {
        try {
            name = appendExtension(name);
            URL stream = Images.class.getClassLoader().getResource(name);
            if (stream == null) throw new IIOException("Resource '" + name + "' does not exist");
            return ImageIO.read(new File(stream.getFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage ofColor(Color color) {
        return ofColor(color, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage ofColor(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int rgb = color.getRGB();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    public static Image fitEntity(Image image) {
        return fit(image, TILE_SIZE, TILE_SIZE + TILE_SIZE);
    }

    public static Image fitTile(Image image) {
        return fit(image, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Resizes but keeps ratio
     */
    public static Image fitHeight(Image image, int height) {
        double nAspectRatio = width(image) / (double) height(image);
        int width = (int) (nAspectRatio * height);

        return resize(image, width, height);
    }

    /**
     * Resizes but keeps ratio
     */
    public static Image fitWidth(Image image, int width) {
        double nAspectRatio = height(image) / (double) width(image);
        int height = (int) (nAspectRatio * width);

        return resize(image, width, height);
    }

    /**
     * Resizes but keeps ratio
     */
    public static Image fit(Image image, int width, int height) {
        double aspectRatio = width(image) / (double) height(image);
        int valuedWidth = (int) (aspectRatio * height);

        if (valuedWidth < width) {
            width = valuedWidth;
        } else {
            height = (int) (width / aspectRatio);
        }

        return resize(image, width, height);
    }

    public static Image resize(Image image, double factor) {
        return resize(image, (int) (width(image) * factor), (int) (height(image) * factor));
    }

    public static Image resize(Image image, int width, int height) {
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static void drawCenter(Graphics graphics, Image image) {
        Rectangle resolution = graphics.getClipBounds();
        drawCenter(graphics, image, halve(resolution.width), halve(resolution.height));
    }

    public static void drawCenter(Graphics graphics, Image image, int x, int y) {
        graphics.drawImage(image,
                x - halve(width(image)),
                y - halve(height(image)),
                null);
    }

    public static int width(Image image) {
        return image.getWidth(null);
    }

    public static int height(Image image) {
        return image.getHeight(null);
    }

    public static BufferedImage crop(Image image, int x, int y) {
        BufferedImage buffer = toBuffer(image);
        int width = buffer.getWidth() - 1;
        int height = buffer.getHeight() - 1;

        x = Math.min(width, Math.max(0, x));
        y = Math.min(height, Math.max(0, y));

        return buffer.getSubimage(x, y, width - x + 1, height - y + 1);
    }

    public static BufferedImage crop(Image image, int x, int y, int w, int h) {
        BufferedImage buffer = toBuffer(image);
        int width = buffer.getWidth() - 1;
        int height = buffer.getHeight() - 1;

        x = Math.min(width, Math.max(0, x));
        y = Math.min(height, Math.max(0, y));

        w = Math.min(width + 1, Math.max(0, w) + x) - x;
        h = Math.min(height + 1, Math.max(0, h) + y) - y;

        return buffer.getSubimage(x, y, w, h);
    }

    public static BufferedImage toBuffer(Image image) {
        if (image instanceof BufferedImage buffer) return buffer;
        return copy(image);
    }

    public static BufferedImage copy(Image image) {
        BufferedImage buffer = new BufferedImage(width(image), height(image), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = buffer.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return buffer;
    }

    private static int halve(int n) {
        return (int) (n * 0.5);
    }
}
