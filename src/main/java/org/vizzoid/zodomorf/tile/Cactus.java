package org.vizzoid.zodomorf.tile;

public class Cactus implements TileBehavior {

    private Tile tile;

    public Cactus(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {

    }

    @Override
    public void update() {

    }
}
