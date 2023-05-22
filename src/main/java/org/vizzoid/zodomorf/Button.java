package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.Rectangle;
import java.awt.event.MouseEvent;

public class Button extends OverlayPart {

    private final Runnable runnable;

    public Button(int x, int y, int width, int height, Runnable runnable) {
        super(x, y, width, height);
        this.runnable = runnable;
    }
    
    public Button(Rectangle rectangle, Runnable runnable) {
        super(rectangle);
        this.runnable = runnable;
    }

    public void press() {
        if (!pressed) runnable.run();
        super.press();
    }

}
