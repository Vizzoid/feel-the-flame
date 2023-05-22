import java.awt.event.KeyEvent;

import org.w3c.dom.events.MouseEvent;

public class OverlayPart {
    
    protected final Rectangle rectangle;
    protected boolean pressed = false;

    public OverlayPart(int x, int y, int width, int height) {
        this(new Rectangle(new ImmoveablePoint(x, y), width, height));
    }
    
    public OverlayPart(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void tryPress(MouseEvent e) {
        if (isPressed(e)) press();
    }

    public void tryRelease(MouseEvent e) {
        if (isPressed(e)) release();
    }

    public void tryPress(KeyEvent e) {
        if (pressed) press(e);
    }

    public boolean isPressed(MouseEvent e) {
        return rectangle.intersects(new ImmoveablePoint(e.getX(), e.getY()));
    }

    public void press(KeyEvent e) {
        
    }

    public void press() {
        pressed = true;
    }

    public void release() {
        pressed = false;
    }

}