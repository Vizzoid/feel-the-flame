package org.vizzoid.zodomorf.tile;

import org.vizzoid.utils.IBuilder;
import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

import java.awt.*;
import java.util.Collection;
import java.util.function.Function;

import static org.vizzoid.zodomorf.engine.Images.*;

public class Material implements TilePainter {

    private static final PresetMap<Material> materials = new PresetMap<>();

    public static final Material EMPTY, OXYGEN, FOUNDATION, COPPER_ORE, 
        SEDIMENTARY_ROCK, IGNEOUS_ROCK, DIRT, LAVA, DEBRIS, CLAY, SILICATE, 
        NICKEL, ASH, GRAVEL, GOLD, SAND, SANDSTONE, ICE, OBSIDIAN, WATER, 
        SULFUR, MERCURY, CORAL, LIMESTONE;
    private static final Color BACKGROUND_SHADE = new Color(0, 0, 0, 140);

    static {
        EMPTY = new Material(builder("empty").gas()) {
            @Override
            public void paint(TileInfo info) {

            }
        };
        OXYGEN = builder("oxygen").gas().build();
        FOUNDATION = builder("foundation").health(10).image(IFOUNDATION).build();
        COPPER_ORE = builder("copper_ore").health(4).image(ICOPPER_ORE).build();
        SEDIMENTARY_ROCK = builder("sedimentary_rock").health(2).image(ISEDIMENTARY_ROCK).build();
        IGNEOUS_ROCK = builder("igneous_rock").health(3).image(IIGNEOUS_ROCK).build();
        DIRT = builder("dirt").health(1).image(IDIRT).build();
        LAVA = builder("lava").settleTicks(8).image(ILAVA).liquid().build();
        DEBRIS = builder("debris").health(1).image(IDEBRIS).build();
        CLAY = builder("clay").health(2).image(ICLAY).build();
        SILICATE = builder("silicate").health(3).image(ISILICATE).build();
        NICKEL = builder("nickel").health(4).image(INICKEL).build();
        ASH = builder("ash").health(1).image(IASH).build();
        GRAVEL = builder("gravel").health(1).image(IGRAVEL).build();
        GOLD = builder("gold").health(4).image(IGOLD).build();
        SAND = builder("sand").health(1).image(ISAND).build();
        SANDSTONE = builder("sandstone").health(2).image(ISANDSTONE).build();
        ICE = builder("ice").health(2).image(IICE).build();
        OBSIDIAN = builder("obsidian").health(20).image(IOBSIDIAN).build();
        WATER = builder("water").settleTicks(2).image(IWATER).liquid().build();
        SULFUR = builder("sulfur").health(3).image(ISULFUR).build();
        MERCURY = builder("mercury").health(4).image(IMERCURY).build();
        CORAL = builder("coral").health(1).image(ICORAL).build();
        LIMESTONE = builder("limestone").health(2).image(ILIMESTONE).build();

        materials.close();
    }


    private static MaterialBuilder builder(String key) {
        return new MaterialBuilder().key(key);
    }

    public static PresetMap<Material> map() {
        return materials;
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
    private final int health;
    private final int settleTicks;
    private final Function<Tile, TileBehavior> behaviorBuilder;

    public Material(MaterialBuilder builder) {
        this.image = builder.image;
        this.type = builder.type;
        this.key = builder.key;
        this.health = builder.health;
        this.settleTicks = builder.settleTicks;
        this.behaviorBuilder = builder.behaviorBuilder;

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

    public int getHealth() {
        return health;
    }

    public int getSettleTicks() {
        return settleTicks;
    }

    public TileBehavior buildBehavior(Tile t) {
        return behaviorBuilder.apply(t);
    }

    private static class MaterialBuilder implements IBuilder<Material> {

        private String key = "empty";
        private Image image;
        private MaterialType type = MaterialType.SOLID;
        private int health = 1;
        private int settleTicks = -1;
        private Function<Tile, TileBehavior> behaviorBuilder = t -> TileBehavior.EMPTY;

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
            return behaviorBuilder(Fluid::new);
        }

        public MaterialBuilder key(String key) {
            this.key = key;
            return this;
        }

        public MaterialBuilder health(int health) {
            this.health = health;
            return this;
        }

        public MaterialBuilder settleTicks(int settleTicks) {
            this.settleTicks = settleTicks;
            return this;
        }

        public MaterialBuilder behaviorBuilder(Function<Tile, TileBehavior> behaviorBuilder) {
            this.behaviorBuilder = behaviorBuilder;
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
