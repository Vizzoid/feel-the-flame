package org.vizzoid.zodomorf.entity;

import org.vizzoid.zodomorf.Planet;

public class Lifespan implements Cloneable {

    public static final Lifespan INFINITE = new Lifespan(-1) {

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public boolean isDead() {
            return false;
        }

        @Override
        public Lifespan clone() {
            return INFINITE;
        }
    };

    private int lengthInTicks;

    public Lifespan(int lengthInTicks) {
        this.lengthInTicks = lengthInTicks;
    }

    public static Lifespan days(int days) {
        return new Lifespan(days * Planet.TIME_IN_DAY);
    }

    public void tick(long ticks) {
        lengthInTicks -= ticks;
    }

    public boolean isAlive() {
        return lengthInTicks > 0;
    }

    public boolean isDead() {
        return lengthInTicks <= 0;
    }

    @Override
    public Lifespan clone() {
        try {
            return (Lifespan) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
