package org.vizzoid.zodomorf.building;

import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;

public interface Buildable {

    Buildable EMPTY = new Buildable() {

        @Override
        public boolean canPlace(Tile tile) {
            return false;
        }

        @Override
        public void place(Tile tile) {

        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Image getImage() {
            return Images.IEMPTY;
        }

        @Override
        public String getName() {
            return "Empty";
        }
    };

    static Buildable[] array() {
        return new Buildable[] {new Cooler(), new Heater(), new Rocket(), new ComfortableRocket(), new MaterialBuildable(Material.FOUNDATION, Material.CLAY, 2)};
    }

    boolean canPlace(Tile tile);

    void place(Tile tile);

    default boolean isEmpty() {
        return false;
    }

    String getName();

    Image getImage();

    default boolean isSame(Buildable buildable) {
        return buildable.getClass().equals(this.getClass());
    }

}
