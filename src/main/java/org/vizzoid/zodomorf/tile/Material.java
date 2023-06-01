package org.vizzoid.zodomorf.tile;

import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.ZBuilder;
import org.vizzoid.zodomorf.engine.TileInfo;
import org.vizzoid.zodomorf.engine.TilePainter;

import java.awt.*;
import java.util.Collection;
import java.util.function.Function;

public class Material implements TilePainter {

    private static final PresetMap<Material> materials = new PresetMap<>();

    public static final Material EMPTY, FOUNDATION, COPPER_ORE,
            SEDIMENTARY_ROCK, IGNEOUS_ROCK, DIRT, LAVA, DEBRIS, CLAY, SILICATE,
            NICKEL, ASH, GRAVEL, GOLD, SAND, SANDSTONE, ICE, OBSIDIAN, WATER,
            SULFUR, MERCURY, CORAL, LIMESTONE, TREE, COMPOSITE, BUILDING,
            STEEL, CLOTH, GLASS, PAPER, PLASTIC, CACTUS, MEAT;
    private static final Color BACKGROUND_SHADE = new Color(0, 0, 0, 140);

    static {
        EMPTY = builder("empty").gas().build();

        DEBRIS = builder("debris").health(1).build();
        DIRT = builder("dirt").health(1).build();
        ASH = builder("ash").health(1).build();
        GRAVEL = builder("gravel").health(1).build();
        SAND = builder("sand").health(1).build();

        LIMESTONE = builder("limestone").health(2).build();
        SANDSTONE = builder("sandstone").health(2).build();
        ICE = builder("ice").health(2).build();
        CLAY = builder("clay").health(2).build();
        SEDIMENTARY_ROCK = builder("sedimentary_rock").health(2).build();

        IGNEOUS_ROCK = builder("igneous_rock").health(3).build();
        SILICATE = builder("silicate").health(3).build();
        SULFUR = builder("sulfur").health(3).build();

        COPPER_ORE = builder("copper_ore").health(4).build();
        NICKEL = builder("nickel").health(4).build();
        GOLD = builder("gold").health(4).build();
        MERCURY = builder("mercury").health(4).build();

        CORAL = builder("coral").behaviorBuilder(LivingCoral::new).health(5).plant().build();
        TREE = builder("tree").behaviorBuilder(TreeTile::new).health(5).plant().build();
        CACTUS = builder("cactus").behaviorBuilder(Cactus::new).health(5).plant().build();
        MEAT = builder("meat").build();

        OBSIDIAN = builder("obsidian").health(6).build();

        WATER = builder("water").settleTicks(2).liquid().build();
        LAVA = builder("lava").settleTicks(8).liquid().build();

        STEEL = builder("steel").health(20).build();
        CLOTH = builder("cloth").health(20).build();
        GLASS = builder("glass").health(20).build();
        PAPER = builder("paper").health(20).build();
        PLASTIC = builder("plastic").health(20).build();

        FOUNDATION = builder("foundation").health(20).build();
        COMPOSITE = builder("composite").health(Integer.MAX_VALUE).build();
        BUILDING = builder("building").health(100).imageFromBehavior().build();

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

    public static Material[] array() {
        return materials.array(new Material[materials.values().size()]);
    }

    public static Material fromKey(String name) {
        return materials.get(name);
    }

    public static Material fromIndex(int index) {
        return materials.get(index);
    }

    private final Image image;
    private final MaterialType type;
    private final String key;
    private final int health;
    private final int settleTicks;
    private final Function<Tile, TileBehavior> behaviorBuilder;
    private final boolean imageFromBehavior;
    private final boolean plant;

    public Material(MaterialBuilder builder) {
        this.image = builder.image;
        this.type = builder.type;
        this.key = builder.key;
        this.health = builder.health;
        this.settleTicks = builder.settleTicks;
        this.behaviorBuilder = builder.behaviorBuilder;
        this.imageFromBehavior = builder.imageFromBehavior;
        this.plant = builder.plant;

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

    public boolean isImageFromBehavior() {
        return imageFromBehavior;
    }

    @Override
    public void paint(TileInfo info) {
        if (isGas()) return;
        info.drawTile(image);
    }

    public void paintBackground(TileInfo info) {
        paint(info);
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

    public void paint(TileInfo info, TileBehavior behavior) {
        if (isGas()) return;
        info.drawTile(!imageFromBehavior ? image : behavior.getImage());
    }

    public void paintBackground(TileInfo info, TileBehavior behavior) {
        paint(info, behavior);
        info.graphics.setColor(BACKGROUND_SHADE);
        info.graphics.fillRect(info.screenX, info.screenY, info.squareSize, info.squareSize);
    }

    public boolean isPlant() {
        return plant;
    }

    private static class MaterialBuilder extends ZBuilder<MaterialBuilder, Material> {

        private MaterialType type = MaterialType.SOLID;
        private int health = 1;
        private int settleTicks = -1;
        private Function<Tile, TileBehavior> behaviorBuilder = t -> TileBehavior.EMPTY;
        private boolean imageFromBehavior = false;
        private boolean plant = false;

        public MaterialBuilder plant() {
            this.plant = true;
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

        public MaterialBuilder imageFromBehavior() {
            this.imageFromBehavior = true;
            return this;
        }
    }

}
