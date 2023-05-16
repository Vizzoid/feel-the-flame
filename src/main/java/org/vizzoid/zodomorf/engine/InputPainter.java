package org.vizzoid.zodomorf.engine;

import org.vizzoid.utils.engine.Painter;

import java.awt.event.*;

public interface InputPainter extends Painter, MouseMotionListener, MouseWheelListener, MouseListener, KeyListener {

    @Override
    default void keyTyped(KeyEvent e) {

    }

    @Override
    default void keyPressed(KeyEvent e) {

    }

    @Override
    default void keyReleased(KeyEvent e) {

    }

    @Override
    default void mouseClicked(MouseEvent e) {

    }

    @Override
    default void mousePressed(MouseEvent e) {

    }

    @Override
    default void mouseReleased(MouseEvent e) {

    }

    @Override
    default void mouseEntered(MouseEvent e) {

    }

    @Override
    default void mouseExited(MouseEvent e) {

    }

    @Override
    default void mouseDragged(MouseEvent e) {

    }

    @Override
    default void mouseMoved(MouseEvent e) {

    }

    @Override
    default void mouseWheelMoved(MouseWheelEvent e) {

    }
}
