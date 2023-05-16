package org.vizzoid.zodomorf;

import org.vizzoid.utils.IBuilder;
import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

import java.awt.*;
import java.util.Collection;

import static org.vizzoid.zodomorf.engine.Images.*;

public class Material implements TilePainter {

    private static final PresetMap<Material> materials = new PresetMap<>();

    public static final Material
            EMPTY, OXYGEN, FOUNDATION, COPPER_ORE, SEDIMENTARY_ROCK, IGNEOUS_ROCK, DIRT, LAVA;
    private static final Color BACKGROUND_SHADE = new Color(0, 0, 0, 140);

    static {
        EMPTY = new Material(builder("empty").gas()) {
            @Override
            public void paint(TileInfo info) {

            }
        };
        OXYGEN = builder("oxygen").gas().build();
        FOUNDATION = builder("foundation").image(foundation()).build();
        COPPER_ORE = builder("copper_ore").image(copperOre()).build();
        SEDIMENTARY_ROCK = builder("sedimentary_rock").rock().image(sedimentaryRock()).build();
        IGNEOUS_ROCK = builder("igneous_rock").rock().image(igneousRock()).build();
        DIRT = builder("dirt").image(dirt()).build();
        LAVA = builder("lava").image(lava()).liquid().build();

        materials.close();
    }


    private static MaterialBuilder builder(String key) {
        return new MaterialBuilder().key(key);
    }

    public static Collection<? extends Material> values() {
        return materials.values();
    }

    public static Material fromKey(String name) {
        return materials.get(name);
    }

    private final Image image;
    private final MaterialType type;
    private final String key;
    private final boolean rock;

    public Material(MaterialBuilder builder) {
        this.image = builder.image;
        this.type = builder.type;
        this.key = builder.key;
        this.rock = builder.rock;

        materials.put(key, this);
    }

    public boolean isRock() {
        return rock;
    }

    public String getKey() {
        return key;
    }

    public Image getImage() {
        return image;
    }

    public MaterialType getType() {
        return type;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isSolid() {
        return type == MaterialType.SOLID;
    }

    @Override
    public void paint(TileInfo info) {
        if (isGas()) return;
        info.drawTile(image);
    }

    public void paintBackground(TileInfo info) {
        if (isGas()) return;
        info.drawTile(image);
        info.graphics.setColor(BACKGROUND_SHADE);
        info.graphics.fillRect(info.screenX, info.screenY, info.squareSize, info.squareSize);
    }

    public boolean isGas() {
        return type == MaterialType.GAS;
    }

    public boolean isLiquid() {
        return type == MaterialType.LIQUID;
    }

    private static class MaterialBuilder implements IBuilder<Material> {

        private String key = "empty";
        private Image image = Images.foundation();
        private MaterialType type = MaterialType.SOLID;
        private boolean rock = false;

        public MaterialBuilder rock() {
            this.rock = true;
            return this;
        }

        public MaterialBuilder image(Image image) {
            this.image = image;
            return this;
        }

        public MaterialBuilder gas() {
            this.type = MaterialType.GAS;
            return this;
        }

        public MaterialBuilder liquid() {
            this.type = MaterialType.LIQUID;
            return this;
        }

        public MaterialBuilder key(String key) {
            this.key = key;
            return this;
        }

        @Override
        public Material build0() {
            return new Material(this);
        }

        @Override
        public void check() {

        }
    }

}
