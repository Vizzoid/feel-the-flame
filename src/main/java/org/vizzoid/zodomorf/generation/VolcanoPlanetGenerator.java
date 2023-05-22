package org.vizzoid.zodomorf.generation;

import org.vizzoid.zodomorf.Latice;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.LivingCoral;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TileBehavior;

import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Random;

public class VolcanoPlanetGenerator implements PlanetGenerator {

    private static final PlanetTileSet VOLCANO = new PlanetTileSet().sea(Material.LAVA).dirt(Material.LAVA).crust(Material.OBSIDIAN).mantle(Material.SULFUR).metal(Material.MERCURY);
    public static final int VOLCANO_SIZE = 129;
    private static final int VOLCANO_DIAMETER = VOLCANO_SIZE + VOLCANO_SIZE;

    @Override
    public PlanetTileSet set() {
        return new PlanetTileSet().sea(Material.LAVA).dirt(Material.ASH).crust(Material.DEBRIS).mantle(Material.OBSIDIAN).metal(Material.EMPTY);
    }

    @Override
    public void populate(Planet planet, PlanetTileSet set) {
        int[] heights = surface(planet, set);
        int[] caveHeights = caveHeights(heights, planet);
        int[] igneousHeights = igneousHeights(planet);
        populateCaves(planet, set, caveHeights, igneousHeights);

        Latice<Tile> latice = planet.getTileLatice();
        int centerX = latice.getWidth() / 2;
        int centerY = (latice.getHeight() * 2) / 3;
        Ellipse2D.Double circle = new Ellipse2D.Double(centerX - VOLCANO_SIZE, centerY - VOLCANO_SIZE, VOLCANO_DIAMETER, VOLCANO_DIAMETER);

        int[] veinHeights = new int[VOLCANO_DIAMETER];
        Arrays.fill(veinHeights, -1);
        for (int x = (int) circle.x,
             endX = centerX + VOLCANO_SIZE,
             startY = (int) circle.y,
             endY = centerY + VOLCANO_SIZE;
             x < endX; x++) {
            int xIndex = (int) (x - circle.x);
            for (int y = startY; y < endY; y++) {
                if (!circle.contains(x, y)) continue;
                if (y > heights[x]) continue;
                if (veinHeights[xIndex] == -1) {
                    veinHeights[xIndex] = y;
                    continue;
                }
                latice.get(x, y).setMaterial(VOLCANO.crust());
            }
        }

        Random r = planet.getRandom();
        int coralSpace = VOLCANO_DIAMETER / 13;
        int coralVariation = coralSpace / 2;

        for (int x = 1; x < 13; x++) {
            int coralX = (x * coralSpace) + (r.nextInt(coralSpace) - coralVariation);

            Tile tile = latice.get(coralX + circle.x, veinHeights[coralX] + 3);
            tile.setMaterial(Material.EMPTY);
            tile.setBehavior(new VolcanoVein(tile));
        }

        settle(planet);

        for (int x = 0; x < veinHeights.length; x++) {
            int height = veinHeights[x];
            if (height == -1) continue;
            for (int y = height + 10; y < 75; y++) {
                Tile tile = latice.get(x + circle.x, y);
                if (y > 70) {
                    tile.setMaterial(VOLCANO.caveAir());
                    continue;
                }
                tile.setMaterial(VOLCANO.sea());
            }
        }

        for (Tile tile : latice) {
            if (tile.getBehavior() instanceof VolcanoVein) tile.setBehavior(TileBehavior.EMPTY);
        }

    }

    @Override
    public void generate(Planet planet) {
        PlanetGenerator.super.generate(planet);
    }

    public void surface(int[] heights, Planet planet, PlanetTileSet set) {
        Latice<Tile> latice = planet.getTileLatice();

        int width = latice.getWidth();
        for (int x = 0; x < width; x++) {
            int heightY = heights[x];

            int dirtHeight = heightY - DIRT_DEPTH();
            for (int y = dirtHeight; y < heightY; y++) {
                latice.set(x, y, new Tile(planet, set.dirt(), x, y));
            }
            for (int y = 0; y < dirtHeight; y++) {
                latice.set(x, y, new Tile(planet, set.crust(), x, y));
            }
            if (heightY <= SEA_LEVEL()) latice.set(x, SEA_LEVEL(), new Tile(planet, Material.IGNEOUS_ROCK, x, heightY));
            for (int y = heightY; y <= SEA_LEVEL() - 1; y++) {
                latice.set(x, y, new Tile(planet, set.sea(), x, y));
            }
        }
    }

    private static class VolcanoVein extends LivingCoral {
        public VolcanoVein(Tile tile) {
            super(tile);
        }

        @Override
        protected int minGrowth() {
            return 0;
        }

        @Override
        protected int maxGrowth() {
            return 1;
        }

        @Override
        protected int minLime() {
            return 0;
        }

        @Override
        protected int maxLime() {
            return 5;
        }

        @Override
        public void tick(long ticks) {
            Planet planet = tile.getPlanet();
            Latice<Tile> latice = planet.getTileLatice();
            int tileX = tile.getX();
            int tileY = tile.getY();
            {
                int coral = 0;
                for (int x = tileX - 1; x <= tileX + 1; x++) {
                    for (int y = tileY - 1; y <= tileY + 1; y++) {
                        if (latice.get(x, y).getMaterial() == tile.getMaterial()) {
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

            tickLime(ticks);
        }

        @Override
        public boolean canGrow(Tile into) {
            return into.getMaterial() == Material.OBSIDIAN;
        }

        @Override
        public void convertLime(Tile into) {
            Random r = into.getPlanet().getRandom();
            if (r.nextInt(4) < 3) into.setMaterial(Material.SULFUR);
            else into.setMaterial(Material.MERCURY);
        }

        @Override
        public void convert(Tile into) {
            into.setMaterial(Material.EMPTY);
            into.setBehavior(new VolcanoVein(into));
        }
    }
}
