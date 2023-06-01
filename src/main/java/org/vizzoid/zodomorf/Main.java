package org.vizzoid.zodomorf;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.zodomorf.engine.PlanetEngine;
import org.vizzoid.zodomorf.generation.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static final boolean DEBUG = true;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        boolean visual = false;

        if (!visual) {
            Game game = new Game();/*
            Planet barren = game.generatePlanet(new BarrenPlanetGenerator());*/
            Planet land = game.generatePlanet(new LandPlanetGenerator());/*
            Planet forest = game.generatePlanet(new ForestPlanetGenerator());
            Planet desert = game.generatePlanet(new DesertPlanetGenerator());
            Planet ocean = game.generatePlanet(new OceanPlanetGenerator());
            Planet volcano = game.generatePlanet(new VolcanoPlanetGenerator());*/
            game.setPlanet(land);
            PlanetEngine.start(game);
            /*new PlanetEngine(game) {
                @Override
                public void newPlanetPainter() {
                    setPainter(new StructurePainter(this.getEngine()));
                }

                @Override
                public void tick(long missedTime) {

                }
            };*/

            /*Thread.sleep(5000);
            game.setPlanet(land);

            Thread.sleep(5000);
            game.setPlanet(forest);

            Thread.sleep(5000);
            game.setPlanet(desert);

            Thread.sleep(5000);
            game.setPlanet(ocean);

            Thread.sleep(5000);
            game.setPlanet(volcano);*/


            /*FileOutputStream fileOutputStream
                    = new FileOutputStream("planet.txt");
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.flush();
            objectOutputStream.close();

            FileInputStream fileInputStream
                    = new FileInputStream("planet.txt");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            Game game1 = (Game) objectInputStream.readObject();
            objectInputStream.close();
            Planet planet1 = game1.getPlanets().get(0);
            planet1.setAvatar(game1.getAvatar());
            PlanetEngine.start(planet1);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    FileOutputStream fileOutputStream1 = new FileOutputStream("planet.txt");
                    ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream1);
                    objectOutputStream1.writeObject(game1);
                    objectOutputStream1.flush();
                    objectOutputStream1.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));*/
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