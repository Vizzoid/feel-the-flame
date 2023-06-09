package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.DefaultEngine;
import org.vizzoid.utils.engine.Sleeper;
import org.vizzoid.zodomorf.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.Executors;

public class PlanetEngine implements InputPainter {

    private static PlanetEngine instance;

    protected final Game game;
    protected final DefaultEngine engine;
    private InputPainter painter;
    private final Sleeper tickSleeper = new Sleeper();

    public PlanetEngine(Game game) {
        this.game = game;
        this.engine = new DefaultEngine() {
            @Override
            protected void prepare() {
                window.setResizable(false);
                display.setFocusable(true);
                window.setUndecorated(true);
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setDimension(toolkit.getScreenSize());
                window.setVisible(true);
            }
        };
        newPlanetPainter();
        engine.addMouseListener(this);
        engine.addKeyListener(this);
        engine.addWheelListener(this);
        engine.addMotionListener(this);
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

    public void newPlanetPainter() {
        setPainter(new PlanetPainter(game.getAvatar().getPlanet(), engine));
    }

    public static void start(Game game) {
        if (instance != null) return;

        instance = new PlanetEngine(game);
    }

    public static PlanetEngine getInstance() {
        return instance;
    }

    public void tick(long missedTime) {
        game.tick(missedTime);

        if (painter instanceof GameOverPainter) return;
        if (game.getAvatar().isDead()) {
            setPainter(new GameOverPainter());
        }
    }

    public DefaultEngine getEngine() {
        return engine;
    }

    public InputPainter getPainter() {
        return painter;
    }

    public void setPainter(InputPainter painter) {
        this.painter = painter;
        engine.setPainter(painter);
    }

    @Override
    public void paint(Graphics graphics, long missedTime) {
        painter.paint(graphics, missedTime);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        painter.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        painter.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        painter.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        painter.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        painter.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        painter.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        painter.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        painter.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        painter.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        painter.mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        painter.mouseWheelMoved(e);
    }
}
