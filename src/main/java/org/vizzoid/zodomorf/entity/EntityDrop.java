package org.vizzoid.zodomorf.entity;

import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.tile.Material;

public class EntityDrop {

    public static final EntityDrop NONE = new EntityDrop(Material.EMPTY, 0) {
        @Override
        public void give(Avatar avatar) {

        }
    };
    private final Material material;
    private final int amount;

    public EntityDrop(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public void give(Avatar avatar) {
        avatar.setStorage(material, avatar.getStorage(material) + amount);
    }
}
