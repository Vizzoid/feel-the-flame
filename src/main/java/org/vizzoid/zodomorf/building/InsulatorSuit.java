package org.vizzoid.zodomorf.building;

import java.awt.Image;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;

public class InsulatorSuit extends Building {

    public InsulatorSuit() {
        super(5, 7);
    }

    @Override
    public String getName() {
        return "Insulator Suit";
    }

    @Override
    public Image getImage() {
        return Images.IINSULATOR_SUIT;
    }

    @Override
    public Material getMaterial() {
        return Material.MERCURY;
    }

    @Override
    public int getCost() {
        return 10;
    }

    @Override
    public void tick(long ticks) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (!avatar.getSuit().equals(this)) return;
        if (!avatar.spend(Material.SILICATE, 1)) avatar.setSuit(null);
    }
    
    @Override
    public void interact() {
        Avatar avatar = tile.getPlanet().getAvatar();
        avatar.setSuit(this);
    }

}
