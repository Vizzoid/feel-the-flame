package org.vizzoid.zodomorf;

import org.vizzoid.zodomorf.tile.Tile;

import java.io.Serial;
import java.io.Serializable;

public class Structure implements Serializable {

    @Serial
    private static final long serialVersionUID = 2496139602475638147L;

    private final Latice<Tile> latice;

    public Structure(int width, int height) {
        this(new Latice<>(width, height));
    }

    public Structure(Latice<Tile> objects) {
        this.latice = objects;
    }

    public Latice<Tile> getLatice() {
        return latice;
    }
}
