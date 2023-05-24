package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;

public class Fluid implements TileBehavior {

    private Tile tile;
    private int liquidSettleTick = 0;
    private boolean move = true;

    public Fluid(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void tick(long ticks) {
        if (!move) return;
        Planet planet = tile.getPlanet();
        Latice<Tile> latice = planet.getTileLatice();
        if ((liquidSettleTick += ticks) > tile.getMaterial().getSettleTicks()) {
            liquidSettleTick = 0;
            int tileX = tile.getX();
            int tileY = tile.getY();

            Tile below = latice.get(tileX, tileY - 1);
            if (below.canMoveInto()) {
                tile.swap(below);
            }
            else {
                for (int x = tileX + 1; x < latice.getWidth(); x++) {
                    Tile side = latice.get(x, tileY);
                    if (side.canMoveInto()) {
                        Tile bottom = latice.get(x, tileY - 1);
                        if (bottom.canMoveInto()) {
                            tile.swap(latice.get(tileX + 1, tileY));
                            return;
                        }
                    } else break;
                }
                for (int x = tileX - 1; x >= 0; x--) {
                    Tile side = latice.get(x, tileY);
                    if (side.canMoveInto()) {
                        Tile bottom = latice.get(x, tileY - 1);
                        if (bottom.canMoveInto()) {
                            tile.swap(latice.get(tileX - 1, tileY));
                            return;
                        }
                    } else break;
                }

                if (below.isLiquid()) {
                    Tile rightTile = latice.get(tileX + 1, tileY);
                    boolean right = rightTile.isLiquid();
                    Tile leftTile = latice.get(tileX - 1, tileY);
                    boolean left = leftTile.isLiquid();
                    if (rightTile.canMoveInto() ^ leftTile.canMoveInto() && right ^ left) {
                        if (right) {
                            tile.swap(leftTile);
                            return;
                        }
                        tile.swap(rightTile);
                        return;
                    }
                }
                move = false;
            }
        }
    }

    public void update() {
        move = true;
    }

    public boolean isMove() {
        return move;
    }
}
