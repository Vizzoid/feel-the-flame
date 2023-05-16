package org.vizzoid.zodomorf;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.zodomorf.engine.PlanetEngine;
import org.vizzoid.zodomorf.generation.NormalPlanetGenerator;
import org.vizzoid.zodomorf.generation.OpenSimplex2S;
import org.vizzoid.zodomorf.generation.PlanetTileSet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        boolean visual = false;

        if (!visual) {
            Game game = new Game();
            Planet planet = new Planet(game.getAvatar());
            game.getPlanets().add(planet);
            PlanetEngine.start(planet);

            new NormalPlanetGenerator(new PlanetTileSet()).generate(planet);
            return;
        }

        DefaultEngine engine = new DefaultEngine() {
            protected void clearScreen(Graphics g) {
            }
        };
        final double[] frequency = {1};
        engine.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> frequency[0] /= 2;
                    case KeyEvent.VK_D -> frequency[0] *= 2;
                }
                System.out.println(frequency[0]);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        long seed = ThreadLocalRandom.current().nextLong();
        engine.getSleeper().setMaxFps(1);
        engine.setPainter((graphics, missedTime) -> {
            for (int y = 0; y < 108; y++) {
                for (int x = 0; x < 192; x++) {
                    double value = OpenSimplex2S.noise3_ImproveXY(seed, x * frequency[0], y * frequency[0], 0);
                    graphics.setColor(value > 0.5 ? Color.WHITE : Color.BLACK);
                    graphics.fillRect(x * 10, y * 10, 10, 10);
                }
            }
        });

    }
}