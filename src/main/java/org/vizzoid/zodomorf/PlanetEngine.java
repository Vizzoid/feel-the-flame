package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.engine.Painter;
import org.vizzoid.utils.engine.Sleeper;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Planet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;

public class PlanetEngine {

    private static PlanetEngine instance;

    protected final Planet planet;
    protected final DefaultEngine engine;
    private Painter painter;
    private final Sleeper tickSleeper = new Sleeper();

    public PlanetEngine(Planet planet) {
        this.planet = planet;
        this.engine = new DefaultEngine() {
            @Override
            protected void prepare() {
                super.prepare();
                window.setResizable(false);
                setDimension(new Dimension(1920, 1080));
            }
        };
        setPainter(newMazePainter());
        Avatar player = planet.getAvatar();
        engine.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
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
        });
        engine.getSleeper().setMaxFps(30);

        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                tickSleeper.setMaxFps(20);
                long missedTime = tickSleeper.pullMissedTime();

                try {
                    tick(missedTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tickSleeper.sleep();
            }
        });
    }

    public PlanetPainter newMazePainter() {
        return new PlanetPainter(planet, engine);
    }

    public static void start(Planet planet) {
        if (instance != null) return;

        instance = new PlanetEngine(planet);
    }

    public void tick(long missedTime) {
        planet.tick(missedTime);

        if (painter instanceof GameOverPainter) return;
        if (planet.getAvatar().isDead()) {
            setPainter(new GameOverPainter());
        }
    }

    public DefaultEngine getEngine() {
        return engine;
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
        engine.setPainter(painter);
    }
}
