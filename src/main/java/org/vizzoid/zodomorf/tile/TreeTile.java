package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;

public class TreeTile implements TileBehavior {

    protected Tile tile;
    private int ticksUntilGrowth = 0;

    public TreeTile(Tile tile) {
        this.tile = tile;
        resetGrowth();
    }

    public void resetGrowth() {
        Planet planet = tile.getPlanet();
        ticksUntilGrowth = planet.getRandom().nextInt(minGrowth(), maxGrowth());
    }

    protected int minGrowth() {
        return 3600;
    }

    protected int maxGrowth() {
        return 13200;
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

        if ((ticksUntilGrowth -= ticks) < 0) {
            resetGrowth();
            Tile into = latice.get(x, y + 1);

            if (canGrow(into)) {
                convert(into);
            }

        }
    }

    public void convert(Tile into) {
        into.setMiddleGround(tile.getMiddleGround());
    }

    public boolean canGrow(Tile into) {
        return !into.isSolid() && !into.getMiddleGround().isSolid();
    }

    @Override
    public void update() {

    }
}
