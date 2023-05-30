package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.IBuilder;
import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.Material;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Collection;

import static org.vizzoid.zodomorf.entity.Lifespan.days;
import static org.vizzoid.zodomorf.entity.Reproduction.*;

public class EntityType {

    private static final PresetMap<EntityType> types = new PresetMap<>();

    public static final EntityType CACTUS_BEAR, FOREST_DEER, METAL_SCORPION, ICE_FISH, ROCK_IGUANA;

    static {
        CACTUS_BEAR = builder("cactus_bear").foodType(Material.CACTUS).deathDrop(new EntityDrop(Material.MEAT, 50)).reproduction(naturalEgg(3 * Planet.TIME_IN_DAY, Material.SULFUR)).build();
        FOREST_DEER = builder("forest_deer").foodType(Material.TREE).lifespan(days(20)).deathDrop(new EntityDrop(Material.MEAT, 400)).reproduction(natural(15 * Planet.TIME_IN_DAY, ForestDeer::new)).build();
        METAL_SCORPION = builder("metal_scorpion").foodType(Material.IGNEOUS_ROCK).deathDrop(new EntityDrop(Material.STEEL, 15)).reproduction(natural(3000, MetalScorpion::new)).build();
        ICE_FISH = builder("ice_fish").foodType(Material.CORAL).lifespan(days(1)).deathDrop(new EntityDrop(Material.MEAT, 20)).reproduction(eat(12000, IceFish::new)).build();
        ROCK_IGUANA = builder("rock_iguana").foodType(Material.LAVA).lifespan(days(10)).eatDrop(new EntityDrop(Material.IGNEOUS_ROCK, 1)).reproduction(new RockIguanaReproduction()).build();
    }

    private static EntityTypeBuilder builder(String key) {
        return new EntityTypeBuilder().key(key);
    }

    public static PresetMap<EntityType> map() {
        return types;
    }

    public static Collection<? extends EntityType> values() {
        return types.values();
    }

    public static EntityType[] array() {
        return types.array(new EntityType[types.values().size()]);
    }

    public static EntityType fromKey(String name) {
        return types.get(name);
    }

    public static EntityType fromIndex(int index) {
        return types.get(index);
    }

    private final String key;
    private final Image image;
    private final Material foodType;
    private final Lifespan lifespan;
    private final EntityDrop deathDrop;
    private final EntityDrop eatDrop;
    private final Reproduction reproduction;
    private final double width;
    private final double height;

    public EntityType(EntityTypeBuilder builder) {
        this.key = builder.key;
        this.image = builder.image;
        this.foodType = builder.foodType;
        this.lifespan = builder.lifespan;
        this.deathDrop = builder.deathDrop;
        this.eatDrop = builder.eatDrop;
        this.reproduction = builder.reproduction;
        this.width = builder.width;
        this.height = builder.height;
    }

    public EntityDrop getDeathDrop() {
        return deathDrop;
    }

    public EntityDrop getEatDrop() {
        return eatDrop;
    }

    public Lifespan getLifespan() {
        return lifespan;
    }

    public Material getFoodType() {
        return foodType;
    }

    public Reproduction getReproduction() {
        return reproduction;
    }

    public Image getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    private static class EntityTypeBuilder implements IBuilder<EntityType> {

        private String key = "empty";
        private Image image;
        private Material foodType = Material.EMPTY;
        private Lifespan lifespan = Lifespan.INFINITE;
        private EntityDrop deathDrop = EntityDrop.NONE;
        private EntityDrop eatDrop = EntityDrop.NONE;
        private Reproduction reproduction = Reproduction.NONE;
        private double width;
        private double height;

        public EntityTypeBuilder width(double width) {
            this.width = width;
            return this;
        }

        public EntityTypeBuilder height(double height) {
            this.height = height;
            return this;
        }

        public EntityTypeBuilder hitbox(double width, double height) {
            return width(width).height(height);
        }

        public EntityTypeBuilder foodType(Material foodType) {
            this.foodType = foodType;
            return this;
        }

        public EntityTypeBuilder lifespan(Lifespan lifespan) {
            this.lifespan = lifespan;
            return this;
        }

        public EntityTypeBuilder deathDrop(EntityDrop deathDrop) {
            this.deathDrop = deathDrop;
            return this;
        }

        public EntityTypeBuilder eatDrop(EntityDrop eatDrop) {
            this.eatDrop = eatDrop;
            return this;
        }

        public EntityTypeBuilder reproduction(Reproduction reproduction) {
            this.reproduction = reproduction;
            return this;
        }

        public EntityTypeBuilder image(Image image) {
            this.image = image;
            return this;
        }

        public EntityTypeBuilder key(String key) {
            this.key = key;
            try {
                Field image = Images.class.getDeclaredField("I" + key.toUpperCase());
                image.setAccessible(true);
                image((Image) image.get(null));
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                image(Images.IDIRT);
            }
            return this;
        }

        @Override
        public void check() {

        }

        @Override
        public EntityType build0() {
            return new EntityType(this);
        }
    }

}
