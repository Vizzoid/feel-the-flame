package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.engine.PlanetEngine;
import org.vizzoid.zodomorf.generation.NormalPlanetGenerator;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Planet planet = new Planet(new Avatar());
        PlanetEngine.start(planet);

        new NormalPlanetGenerator(Material.LAVA).generate(planet);

        /*DefaultEngine engine = new DefaultEngine() {
            protected void clearScreen(Graphics g) {
            }
        };
        engine.addMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        var ref = new Object() {
            int i = 0;
        };
        long seed = ThreadLocalRandom.current().nextLong();
        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        engine.getSleeper().setMaxFps(1);
        engine.setPainter((graphics, missedTime) -> {
            graphics.drawImage(image, 0, 0, null);
        });
        while (true) {
            for (int y = 0; y < 108; y++) {
                for (int x = 0; x < 192; x++) {
                    double value = OpenSimplex2S.noise3_ImproveXY(seed, x * (1 / 24.0), y * (1 / 24.0), ref.i++);
                    int rgb = 0x010101 * (int) ((value + 1) * 127.5);
                    image.setRGB(x * 10, y * 10, rgb);
                }
            }
            Thread.sleep(2000);
        }*/

    }
}