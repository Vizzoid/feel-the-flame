package org.vizzoid.zodomorf.tile;

import java.util.Random;

import org.vizzoid.zodomorf.Planet;

public class LivingCoral implements TileBehavior {

    private Tile tile;
    private int ticksUntilGrowth = 0;
    private int ticksUntilLime = 0;

    public LivingCoral(Tile tile) {
        this.tile = tile;
        resetGrowth();
        resetLime();
    }

    private void resetGrowth() {
        Planet planet = tile.getPlanet();
        ticksUntilGrowth = planet.getRandom().nextInt(3600, 13200); 
    }

    private void resetLime() {
        Planet planet = tile.getPlanet();
        ticksUntilLime = planet.getRandom().nextInt(1200, 6000); 
    }

    @Override
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public void tick(long ticks) {
        if ((ticksUntilGrowth -= ticks) < 0) {
            resetGrowth();
            
            Planet planet = tile.getPlanet();
            Random r = planet.getRandom();
            boolean left = r.nextBoolean();
            int x = (left ? -1 : 1) + tile.getX();
            int y = (r.nextInt(2)) + tile.getY();
            
            Tile into = planet.getTileLatice().get(x, y);
            
            if (into.getMaterial() == Material.WATER) {
                into.setMaterial(tile.getMaterial());
            }
            
        }
        if ((ticksUntilLime -= ticks) < 0) {
            resetLime();
            
            Planet planet = tile.getPlanet();
            Random r = planet.getRandom();
            int direction = r.nextInt(4);
            int x = tile.getX();
            int y = tile.getY();

            switch (direction) {
                case 0 -> {
                    x++;
                }
                case 1 -> {
                    x--;
                }
                case 2 -> {
                    y++;
                }
                case 3 -> {
                    y--;
                }
            }
            
            Tile into = planet.getTileLatice().get(x, y);
            
            if (into.getMaterial() == Material.WATER) {
                into.setMaterial(Material.LIMESTONE);
            }
        }
    }

    @Override
    public void update() {
        
    }
    
}
