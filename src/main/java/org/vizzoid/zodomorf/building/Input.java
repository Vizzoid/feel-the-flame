package org.vizzoid.zodomorf.building;

import org.vizzoid.utils.position.Rectangle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class Input extends OverlayPart {

    private final Consumer<String> input;
    private final Type type;
    private String string;

    public Input(int x, int y, int width, int height, Consumer<String> input) {
        this(x, y, width, height, input, Type.ALL);
    }

    public Input(int x, int y, int width, int height, Consumer<String> input, Type type) {
        super(x, y, width, height);
        this.input = input;
        this.type = type;
    }

    public Input(Rectangle rectangle, Consumer<String> input) {
        this(rectangle, input, Type.ALL);
    }

    public Input(Rectangle rectangle, Consumer<String> input, Type type) {
        super(rectangle);
        this.input = input;
        this.type = type;
    }

    @Override
    public void press() {
        string = "";
        super.press();
    }

    public void press(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            input.accept(string);
            pressed = false;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            string = string.substring(0, string.length() - 1);
            return;
        }
        if (type.is(e.getKeyChar())) {
            string += e.getKeyChar();
        }
    }

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Unimplemented method 'getImage'");
    }

    public enum Type {
        ALL {
            @Override
            public boolean is(char c) {
                return Character.isLetterOrDigit(c);
            }
        },
        NUMBERS {
            @Override
            public boolean is(char c) {
                return Character.isDigit(c);
            }
        },
        LETTERS {
            @Override
            public boolean is(char c) {
                return Character.isAlphabetic(c);
            }
        };

        public abstract boolean is(char c);
    }

}