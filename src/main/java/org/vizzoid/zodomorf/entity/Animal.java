package org.vizzoid.zodomorf.entity;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.Planet;
import org.vizzoid.zodomorf.entity.ai.AnimalAI;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;

public abstract class Animal implements Entity {

    protected Planet planet;
    protected final Material foodType;
    protected final Lifespan lifespan;
    protected final EntityDrop deathDrop;
    protected final EntityDrop eatDrop;
    protected final Reproduction reproduction;
    protected final EntityType type;
    protected final Hitbox hitbox;
    protected int hunger = 20000;
    protected int health = 10;
    protected final AnimalAI ai;

    public Animal(Planet planet, EntityType type, double x, double y) {
        this(planet, type, new MoveablePoint(x, y));
    }

    public Animal(Planet planet, EntityType type, MoveablePoint point) {
        this.planet = planet;
        this.type = type;
        this.foodType = type.getFoodType();
        this.lifespan = type.getLifespan().clone();
        this.deathDrop = type.getDeathDrop();
        this.eatDrop = type.getEatDrop();
        this.reproduction = type.getReproduction().clone();
        this.reproduction.setAnimal(this);
        this.hitbox = new Hitbox(point, type.getWidth(), type.getHeight(), Point.EMPTY);
        this.ai = new AnimalAI(this);

        planet.addEntity(this);
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public Optional<Point> getTarget() {
        return ai.getTarget();
    }

    @Override
    public Image getImage() {
        return type.getImage();
    }

    @Override
    public Planet getPlanet() {
        return planet;
    }

    public int getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    @Override
    public MoveablePoint getPos() {
        return hitbox.getPos();
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    @Override
    public void tick(long ticks) {
        hunger -= ticks;
        lifespan.tick(ticks);
        if (shouldHurt()) health -= ticks;
        if (shouldDie()) {
            die();
            return;
        }
        ai.tick(ticks);
        reproduction.tick(ticks);
    }

    public boolean shouldDie() {
        return health < 0 || lifespan.isDead();
    }

    public boolean shouldHurt() {
        return hunger < 0;
    }

    public boolean shouldFindFood() {
        return hunger < 4000;
    }

    public boolean canEat(Tile tile) {
        return tile.getMaterial() == foodType;
    }

    public void eat(Tile tile) {
        tile.setMaterial(Material.EMPTY);
        eatDrop.give(planet.getAvatar());
        reproduction.onEat();
        hunger = 20000;
    }

    public void die() {
        planet.removeEntity(this);
    }

    public void kill() {
        die();
        deathDrop.give(planet.getAvatar());
    }

    public void hurt() {
        health--;
        if (health < 0) die();
    }

    public Tile getTile() {
        return planet.getTileLatice().get(getPos());
    }
}