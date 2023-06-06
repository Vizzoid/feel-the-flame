package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;

public class StraightFallingTile implements TileBehavior {

    protected Tile tile;

    public StraightFallingTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        Planet planet = tile.getPlanet();
        Latice<Tile> latice = planet.getTileLatice();
        int x = tile.getX();
        int y = tile.getY();
        Tile below = latice.get(x, y - 1);
        if (!below.isSolid() && !below.getMiddleGround().isSolid()) {
            below.setMiddleGround(Material.TREE);
            tile.setMiddleGround(Material.EMPTY);

            tile.setMiddleGroundBehavior(TileBehavior.EMPTY);
            below.setMiddleGroundBehavior(this);

            setTile(below);
            return;
        }
    }

    @Override
    public void update() {

    }
}
