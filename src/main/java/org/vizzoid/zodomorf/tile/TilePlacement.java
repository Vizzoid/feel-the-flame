package org.vizzoid.zodomorf.tile;

public enum TilePlacement {

    FRONT {
        @Override
        public void setMaterial(Tile tile, Material material, boolean updateBehavior, boolean updateBackground) {
            tile.setMaterial(material, updateBehavior, updateBackground);
        }
    },
    MIDDLE {
        @Override
        public void setMaterial(Tile tile, Material material, boolean updateBehavior, boolean updateBackground) {
            tile.setMiddleGround(material, updateBehavior);
        }
    },
    BACK {
        @Override
        public void setMaterial(Tile tile, Material material, boolean updateBehavior, boolean updateBackground) {
            tile.setBackground(material);
        }
    };

    public void setMaterial(Tile tile, Material material) {
        setMaterial(tile, material, true);
    }

    public void setMaterial(Tile tile, Material material, boolean updateBehavior) {
        setMaterial(tile, material, updateBehavior, true);
    }

    public abstract void setMaterial(Tile tile, Material material, boolean updateBehavior, boolean updateBackground);

}
