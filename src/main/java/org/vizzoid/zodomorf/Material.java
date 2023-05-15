package org.vizzoid.zodomorf;

import org.vizzoid.utils.IBuilder;
import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

import java.awt.*;
import java.util.Collection;

public class Material implements TilePainter {

    private static final PresetMap<Material> materials = new PresetMap<>();

    public static final Material
            EMPTY, OXYGEN, FOUNDATION, COPPER_ORE, SEDIMENTARY_ROCK, IGNEOUS_ROCK, DIRT, LAVA;
    private static final Color BACKGROUND_SHADE = new Color(0, 0, 0, 140);

    static {
        EMPTY = new Material(builder("empty").type(MaterialType.GAS)) {
            @Override
            public void paint(TileInfo info) {

            }
        };
        OXYGEN = builder("oxygen").type(MaterialType.GAS).build();
        FOUNDATION = builder("foundation").image(Images.foundation()).type(MaterialType.SOLID).build();
        COPPER_ORE = builder("copper_ore").image(Images.copperOre()).type(MaterialType.SOLID).build();
        SEDIMENTARY_ROCK = builder("sedimentary_rock").image(Images.sedimentaryRock()).type(MaterialType.SOLID).build();
        IGNEOUS_ROCK = builder("igneous_rock").image(Images.igneousRock()).type(MaterialType.SOLID).build();
        DIRT = builder("dirt").image(Images.dirt()).type(MaterialType.SOLID).build();
        LAVA = builder("lava").image(Images.lava()).type(MaterialType.LIQUID).build();

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

    public Material(MaterialBuilder builder) {
        this.image = builder.image;
        this.type = builder.type;
        this.key = builder.key;

        materials.put(key, this);
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
        if (isEmpty()) return;
        info.drawTile(image);
    }

    public void paintBackground(TileInfo info) {
        if (isEmpty()) return;
        info.drawTile(image);
        info.graphics.setColor(BACKGROUND_SHADE);
        info.graphics.fillRect(info.screenX, info.screenY, info.squareSize, info.squareSize);
    }

    private static class MaterialBuilder implements IBuilder<Material> {

        private String key = "empty";
        private Image image = Images.foundation();
        private MaterialType type = MaterialType.SOLID;

        public MaterialBuilder image(Image image) {
            this.image = image;
            return this;
        }

        public MaterialBuilder type(MaterialType type) {
            this.type = type;
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
