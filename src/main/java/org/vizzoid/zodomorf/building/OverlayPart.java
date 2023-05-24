package org.vizzoid.zodomorf.building;

import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.utils.position.Rectangle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class OverlayPart {

    protected final Rectangle rectangle;
    protected boolean pressed = false;

    public OverlayPart(int x, int y, int width, int height) {
        this(new Rectangle(new ImmoveablePoint(x, y), width, height));
    }

    public OverlayPart(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void tryPress(MouseEvent e) {
        if (isInside(e)) press();
    }

    public void tryRelease(MouseEvent e) {
        if (isInside(e)) release();
    }

    public void tryPress(KeyEvent e) {
        if (pressed) press(e);
    }

    public void tryRelease(KeyEvent e) {
        if (pressed) release(e);
    }

    public boolean isInside(MouseEvent e) {
        return rectangle.intersects(new ImmoveablePoint(e.getX(), e.getY()));
    }

    public void press(KeyEvent e) {

    }

    public void release(KeyEvent e) {

    }

    public void press() {
        pressed = true;
    }

    public void release() {
        pressed = false;
    }

    public abstract Image getImage();

    public void paint(Graphics g) {
        Point point = rectangle.getPos();
        g.drawImage(getImage(), (int) point.getX(), (int) point.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight(), null);
    }

}