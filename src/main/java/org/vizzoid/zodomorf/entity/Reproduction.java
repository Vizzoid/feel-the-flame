package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.tile.Material;

import java.util.function.BiFunction;

public class Reproduction implements Cloneable {

    public static final Reproduction NONE = new Reproduction(-1) {
        @Override
        public void reproduce() {

        }

        @Override
        public void tick(long ticks) {

        }

        @Override
        public Reproduction clone() {
            return NONE;
        }

        @Override
        public void setAnimal(Animal animal) {

        }
    };

    private final int maxTime;
    protected Animal animal;
    protected int time;

    public Reproduction(int maxTime) {
        this.time = maxTime;
        this.maxTime = maxTime;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void tick(long ticks) {
        time -= ticks;
        if (time < 0) {
            time = maxTime;
            reproduce();
        }
    }

    public void onEat() {

    }

    public void reproduce() {

    }

    public static Reproduction naturalEgg(int maxTime, Material eggType) {
        return new Reproduction(maxTime) {
            @Override
            public void reproduce() {
                animal.getTile().setMiddleGround(eggType);
            }
        };
    }

    public static Reproduction eatEgg(int maxTime, Material eggType) {
        return new Reproduction(Integer.MAX_VALUE) {
            @Override
            public void onEat() {
                if (time < maxTime) return;
                time = maxTime;
            }
            @Override
            public void reproduce() {
                animal.getTile().setMiddleGround(eggType);
            }
        };
    }

    public static Reproduction natural(int maxTime, BiFunction<Planet, MoveablePoint, Animal> function) {
        return new Reproduction(maxTime) {
            @Override
            public void reproduce() {
                function.apply(animal.getPlanet(), animal.getPos().copyMoveable());
            }
        };
    }

    public static Reproduction eat(int maxTime, BiFunction<Planet, MoveablePoint, Animal> function) {
        return new Reproduction(Integer.MAX_VALUE) {
            @Override
            public void onEat() {
                if (time < maxTime) return;
                time = maxTime;
            }
            @Override
            public void reproduce() {
                function.apply(animal.getPlanet(), animal.getPos().copyMoveable());
            }
        };
    }

    @Override
    public Reproduction clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Reproduction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
