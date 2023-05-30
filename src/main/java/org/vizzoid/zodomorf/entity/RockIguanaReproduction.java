package org.vizzoid.zodomorf.entity;

import org.vizzoid.zodomorf.Planet;

public class RockIguanaReproduction extends Reproduction {
    public RockIguanaReproduction() {
        super(Integer.MAX_VALUE);
    }
    @Override
    public void onEat() {
        int maxTime = Planet.TIME_IN_DAY * 6;
        if (time < maxTime) {
            time -= 1000;
            return;
        }
        time = maxTime;
    }
    @Override
    public void reproduce() {
        new RockIguana(animal.getPlanet(), animal.getPos().copyMoveable());
    }
}
