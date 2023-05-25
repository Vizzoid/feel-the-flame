package org.vizzoid.zodomorf.building;

import org.apache.commons.lang3.StringUtils;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;

public class MaterialBuildable implements Buildable {

    private final Material placed;
    private final Material ingredient;
    private final int cost;

    public MaterialBuildable(Material placed) {
        this(placed, placed, 1);
    }

    public MaterialBuildable(Material placed, Material ingredient, int cost) {
        this.placed = placed;
        this.ingredient = ingredient;
        this.cost = cost;
    }


    @Override
    public boolean canPlace(Tile tile) {
        Avatar avatar = tile.getPlanet().getAvatar();
        if (avatar.getHitbox().intersects(tile.getPos())) return false;
        if (avatar.getStorage(ingredient) < cost) return false;
        if (tile.isSolid()) return false;
        return !tile.getMiddleGround().isSolid();
    }

    @Override
    public void place(Tile tile) {
        tile.setMaterial(placed, true, false);
        tile.getPlanet().getAvatar().spend(ingredient, cost);
    }

    @Override
    public String getName() {
        return StringUtils.capitalize(placed.getKey());
    }

    @Override
    public Image getImage() {
        return placed.getImage();
    }
}
