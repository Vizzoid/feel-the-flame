package org.vizzoid.zodomorf.tile;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;

import java.util.Random;

public class LivingCoral implements TileBehavior {

    protected Tile tile;
    protected int ticksUntilGrowth = 0;
    protected int ticksUntilLime = 0;

    public LivingCoral(Tile tile) {
        this.tile = tile;
        resetGrowth();
        resetLime();
    }

    public void resetGrowth() {
        Planet planet = tile.getPlanet();
        ticksUntilGrowth = planet.getRandom().nextInt(minGrowth(), maxGrowth());
    }

    public void resetLime() {
        Planet planet = tile.getPlanet();
        ticksUntilLime = planet.getRandom().nextInt(minLime(), maxLime());
    }

    protected int minGrowth() {
        return 15000;
    }

    protected int maxGrowth() {
        return 30000;
    }

    protected int minLime() {
        return 45000;
    }

    protected int maxLime() {
        return 90000;
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        tickGrowth(ticks);
        tickLime(ticks);
    }

    public void tickGrowth(long ticks) {
        Planet planet = tile.getPlanet();
        Latice<Tile> latice = planet.getTileLatice();
        int tileX = tile.getX();
        int tileY = tile.getY();
        {
            int coral = 0;
            for (int x = tileX - 1; x <= tileX + 1; x++) {
                for (int y = tileY - 1; y <= tileY + 1; y++) {
                    if (latice.get(x, y).getMiddleGround() == tile.getMiddleGround()) {
                        if (++coral >= 4) return;
                    }
                }
            }
        }

        if ((ticksUntilGrowth -= ticks) < 0) {
            resetGrowth();

            Random r = planet.getRandom();
            int x = r.nextInt(3) + tileX - 1;

            Tile into = latice.get(x, tileY + 1);

            if (canGrow(into)) {
                convert(into);
            }

        }
    }

    public void tickLime(long ticks) {
        if ((ticksUntilLime -= ticks) < 0) {
            resetLime();

            Planet planet = tile.getPlanet();
            Random r = planet.getRandom();
            int direction = r.nextInt(4);
            int x = tile.getX();
            int y = tile.getY();

            switch (direction) {
                case 0 -> x++;
                case 1 -> x--;
                case 2 -> y++;
                case 3 -> y--;
            }

            Tile into = planet.getTileLatice().get(x, y);

            if (canGrow(into)) {
                convertLime(into);
            }
        }
    }

    public void convert(Tile into) {
        into.setMiddleGround(tile.getMiddleGround());
    }

    public void convertLime(Tile into) {
        into.setMiddleGround(Material.LIMESTONE);
    }

    public boolean canGrow(Tile into) {
        return into.getMaterial() == Material.WATER || into.getMiddleGround() == Material.LIMESTONE || into.getMaterial() == Material.ICE;
    }

    @Override
    public void update() {
        
    }
    
}
