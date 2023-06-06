package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;

public class TreeTile extends StraightFallingTile {

    private int ticksUntilGrowth = 0;

    public TreeTile(Tile tile) {
        super(tile);
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
        super.tick(ticks);
        Planet planet = tile.getPlanet();
        Latice<Tile> latice = planet.getTileLatice();
        int x = tile.getX();
        int y = tile.getY();

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
