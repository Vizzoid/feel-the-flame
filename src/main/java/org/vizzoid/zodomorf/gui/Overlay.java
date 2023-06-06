package org.vizzoid.zodomorf.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Overlay {
    
    private final List<Button> buttons = new ArrayList<>();

    public Overlay() {
        
    }

    public void paint(Graphics g) {
        for (Button button : buttons) {
            button.paint(g);
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void removeButton(Button button) {
        buttons.remove(button);
    }

}
