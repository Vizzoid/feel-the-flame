package org.vizzoid.zodomorf.tile;

import java.util.Random;

/**
 * Tile from breakdown of meteor debris
 */
public class MeteorTile extends FallingTile {

    private final Random random;
    private final Material first;
    private final Material second;
    private int breakdownTicks;

    public MeteorTile(Tile tile, Material first, Material second) {
        super(tile);
        this.random = tile.getPlanet().getRandom();
        this.first = first;
        this.second = second;
        breakdownTicks = random.nextInt(600, 1200);
    }

    public boolean canBreakdownSuccessfully() {
        return tile.getPlanet().getTileLatice().get(tile.getX(), tile.getY() - 1).getMaterial() != Material.DEBRIS;
    }

    @Override
    public void tick(long ticks) {
        super.tick(ticks);

        if ((breakdownTicks -= ticks) < 0) {
            Material material = Material.EMPTY;

            if (canBreakdownSuccessfully()) {
                switch (random.nextInt(10)) {
                    case 0 -> material = first;
                    case 1 -> material = second;
                }
            }
            tile.setMaterial(material, true, false);
            if (material != Material.EMPTY) {
                tile.setBehavior(new MeteorTile(tile, Material.NICKEL, Material.COPPER_ORE));
            }
        }
    }
}
