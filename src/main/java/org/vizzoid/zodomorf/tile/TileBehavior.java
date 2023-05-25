package org.vizzoid.zodomorf.tile;

import java.awt.*;

public interface TileBehavior {

    TileBehavior EMPTY = new TileBehavior() {
        @Override
        public void tick(long ticks) {

        }

        @Override
        public void update() {

        }

        @Override
        public void setTile(Tile tile) {

        }
    };


    void setTile(Tile tile);

    void tick(long ticks);

    void update();

    default Image getImage() {
        throw new UnsupportedOperationException();
    }

    default void interact() {

    }

}
