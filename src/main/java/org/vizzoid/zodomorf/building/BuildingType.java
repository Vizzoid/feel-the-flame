package org.vizzoid.zodomorf.building;

import org.vizzoid.utils.PresetMap;
import org.vizzoid.zodomorf.Avatar;
import org.vizzoid.zodomorf.Main;
import org.vizzoid.zodomorf.ZBuilder;
import org.vizzoid.zodomorf.building.power.Battery;
import org.vizzoid.zodomorf.building.power.Wire;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;
import org.vizzoid.zodomorf.tile.TilePlacement;

import java.awt.*;
import java.util.Collection;

public class BuildingType {

    private static final PresetMap<BuildingType> types = new PresetMap<>();

    public static final BuildingType EMPTY, COMFORTABLE_ROCKET, COOLER, HEATER, INSULATOR_SUIT, ROCKET,
            TRIMMER, FOUNDATION, WALL, BATTERY, WIRE;

    static {
        EMPTY = builder("empty").unplaceable().build();
        COMFORTABLE_ROCKET = builder("comfortable_rocket").cost(Material.STEEL, 30).hitbox(3, 8).building(ComfortableRocket::new).build();
        ROCKET = builder("rocket").cost(Material.NICKEL, 30).hitbox(3, 8).building(Rocket::new).build();
        COOLER = builder("cooler").cost(Material.GOLD, 20).hitbox(3, 2).building(Cooler::new).build();
        HEATER = builder("heater").cost(Material.COPPER_ORE, 20).hitbox(3, 2).building(Heater::new).build();
        INSULATOR_SUIT = builder("insulator_suit").cost(Material.MERCURY, 10).hitbox(5, 7).building(InsulatorSuit::new).build();
        TRIMMER = builder("trimmer").cost(Material.COPPER_ORE, 20).hitbox(5, 7).building(Trimmer::new, false).build();
        FOUNDATION = builder("foundation").cost(Material.CLAY, 2).tile(Material.FOUNDATION).build();
        WALL = builder("wall").cost(Material.CLAY, 2).form(new BuildingForm.TileBuild(Material.FOUNDATION, TilePlacement.BACK)).build();

        // power
        BATTERY = builder("battery").cost(Material.GOLD, 15).hitbox(2, 3).building(Battery::new).build();
        WIRE = builder("wire").cost(Material.COPPER_ORE, 1).building(Wire::new).build();

        types.close();
    }

    private static BuildingTypeBuilder builder(String key) {
        return new BuildingTypeBuilder().key(key);
    }

    public static PresetMap<BuildingType> map() {
        return types;
    }

    public static Collection<? extends BuildingType> values() {
        return types.values();
    }

    public static BuildingType[] array() {
        return types.array(new BuildingType[types.values().size()]);
    }

    public static BuildingType fromKey(String name) {
        return types.get(name);
    }

    public static BuildingType fromIndex(int index) {
        return types.get(index);
    }

    private final String key;
    private final Image image;
    private final int width;
    private final int height;
    private final String name;
    private final Material material;
    private final int cost;
    private final BuildingForm form;

    public BuildingType(BuildingTypeBuilder builder) {
        this.key = builder.key;
        this.image = builder.image;
        this.width = builder.width;
        this.height = builder.height;
        this.name = builder.name;
        this.material = builder.material;
        this.cost = builder.cost;
        this.form = builder.form;

        types.put(key, this);
    }

    public String getName() {
        return name;
    }

    public BuildingForm getForm() {
        return form;
    }

    public Image getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canPlace(Tile tile) {
        return form.canPlace(this, tile);
    }

    public void place(Tile tile) {
        form.place(this, tile);
    }

    private static class BuildingTypeBuilder extends ZBuilder<BuildingTypeBuilder, BuildingType> {

        private int width = 1;
        private int height = 1;
        private Material material = Material.EMPTY;
        private int cost = 0;
        private BuildingForm form = new BuildingForm.TileBuild(Material.EMPTY);

        public BuildingTypeBuilder form(BuildingForm form) {
            this.form = form;
            return this;
        }

        public BuildingTypeBuilder building(Builder builder) {
            return building(builder, true);
        }

        public BuildingTypeBuilder building(Builder builder, boolean onFloor) {
            return form(new BuildingForm.BuildingBuild(builder, onFloor));
        }

        public BuildingTypeBuilder tile(Material placed) {
            return form(new BuildingForm.TileBuild(placed));
        }

        public BuildingTypeBuilder cost(Material material, int cost) {
            return material(material).cost(cost);
        }

        public BuildingTypeBuilder material(Material material) {
            this.material = material;
            return this;
        }

        public BuildingTypeBuilder cost(int cost) {
            this.cost = cost;
            return this;
        }

        public BuildingTypeBuilder width(int width) {
            this.width = width;
            return this;
        }

        public BuildingTypeBuilder height(int height) {
            this.height = height;
            return this;
        }

        public BuildingTypeBuilder hitbox(int width, int height) {
            return width(width).height(height);
        }

        @Override
        public BuildingType build0() {
            return new BuildingType(this);
        }

        @Override
        public void check() {
        }

        public BuildingTypeBuilder unplaceable() {
            return form(BuildingForm.UNPLACEABLE);
        }
    }

    private interface Builder {
        Building build(Tile tile);
    }

    public interface BuildingForm {
        class BuildingBuild implements BuildingForm {

            private final Builder builder;
            private final boolean onFloor;

            public BuildingBuild(Builder builder, boolean onFloor) {
                this.builder = builder;
                this.onFloor = onFloor;
            }

            public boolean canPlace(BuildingType type, Tile tile) {
                if (Main.DEBUG) return true;
                Avatar avatar = tile.getPlanet().getAvatar();
                if (avatar.getStorage(type.material) < type.cost) return false;
                for (int x = 0; x < type.width; x++) {
                    for (int y = 0; y < type.height; y++) {
                        Tile relative = tile.relative(x, y);
                        if (relative.isSolid() || relative.getMiddleGround().isSolid()) return false;
                    }
                }
                if (!onFloor) return true;
                for (int x = 0; x < type.width; x++) {
                    Tile relative = tile.relative(x, -1);
                    if (!relative.isSolid()) return false;
                }
                return true;
            }

            public void place(BuildingType type, Tile tile) {
                Avatar avatar = tile.getPlanet().getAvatar();
                avatar.spend(type.material, type.cost);
                Building building = builder.build(tile);
                for (int x = 0; x < type.width; x++) {
                    for (int y = 0; y < type.height; y++) {
                        Tile relative = tile.relative(x, y);
                        relative.setMaterial(Material.EMPTY);
                        relative.setMiddleGround(Material.COMPOSITE);
                        relative.setMiddleGroundBehavior(building.getComposite());
                    }
                }
                tile.setMiddleGroundBehavior(building);
                tile.setMiddleGround(Material.BUILDING, false);
            }
        }
        class TileBuild implements BuildingForm {

            private final Material placed;
            private final TilePlacement placement;

            public TileBuild(Material placed) {
                this(placed, TilePlacement.FRONT);
            }

            public TileBuild(Material placed, TilePlacement placement) {
                this.placed = placed;
                this.placement = placement;
            }
            
            @Override
            public boolean canPlace(BuildingType type, Tile tile) {
                if (Main.DEBUG) return true;

                Avatar avatar = tile.getPlanet().getAvatar();
                // potential bug if player intersects with tile but not at tile pos
                if (avatar.getHitbox().intersects(tile.getPos())) return false;
                if (avatar.getStorage(type.material) < type.cost) return false;
                if (tile.isSolid()) return false;
                return !tile.getMiddleGround().isSolid();
            }

            @Override
            public void place(BuildingType type, Tile tile) {
                placement.setMaterial(tile, placed, true, false);
                tile.getPlanet().getAvatar().spend(type.material, type.cost);
            }
        };

        BuildingForm UNPLACEABLE = new BuildingForm() {
            @Override
            public boolean canPlace(BuildingType type, Tile tile) {
                return false;
            }

            @Override
            public void place(BuildingType type, Tile tile) {

            }
        };

        boolean canPlace(BuildingType type, Tile tile);
        void place(BuildingType type, Tile tile);

    }

}
