package org.vizzoid.zodomorf.tile;

public class DebrisTile extends MeteorTile {

    public DebrisTile(Tile tile) {
        super(tile, Material.CLAY, Material.SILICATE);
    }

    public boolean canBreakdownSuccessfully() {
        return !(tile.getPlanet().getTileLatice().get(tile.getX(), tile.getY() - 1).getBehavior() instanceof MeteorTile);
    }

}
