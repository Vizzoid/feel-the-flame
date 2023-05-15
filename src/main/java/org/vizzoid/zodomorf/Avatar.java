package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.*;
import org.vizzoid.zodomorf.engine.LaticeCamera;

public class Avatar implements LaticeCamera {

    public static final double HEALTH_PER_STARVE = 0.016666666666666666;
    public static final int MAX_VELOCITY = 5;
    private static final double GRAVITY = -0.01;

    private Planet planet;
    private double health = 100;
    private double food = 30000;
    private double temperature = 100;
    private boolean warmingUp = false;
    private final DynamicRectangle hitbox = new DynamicRectangle(new ImmoveablePoint(500, 500), 1, 2, new MoveablePoint());

    public Avatar() {

    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public MoveablePoint getPos() {
        return hitbox.getPos();
    }

    public MoveablePoint getVelocity() {
        return hitbox.getVelocity();
    }

    public double getX() {
        return getPos().getX();
    }

    public double getY() {
        return getPos().getY();
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public boolean isOutside() {
        return getTile().getBackground().isEmpty();
    }

    public boolean isTooCold() {
        return temperature < 10;
    }

    public boolean isWarmingUp() {
        return warmingUp;
    }

    public Tile getTile() {
        return planet.getTileLatice().get(getTileX(), getTileY());
    }

    public void tick(long ticks) {
        Latice<Tile> latice = planet.getTileLatice();
        MoveablePoint velocity = getVelocity();
        System.out.println(velocity);
        velocity.moveY(GRAVITY);

        // we don't want velocity too fast that it cannot collide with tiles or lag the game
        // we also don't want to limit it to length as it means you can prevent your fall by moving left and right
        if (velocity.getX() > MAX_VELOCITY) {
            velocity.setX(MAX_VELOCITY);
        }
        if (velocity.getY() > MAX_VELOCITY) {
            velocity.setY(MAX_VELOCITY);
        }
        if (velocity.getX() < -MAX_VELOCITY) {
            velocity.setX(-MAX_VELOCITY);
        }
        if (velocity.getY() < -MAX_VELOCITY) {
            velocity.setY(-MAX_VELOCITY);
        }


        {
            // we are splitting the collisions into above feet and below feet so that we don't add gravity velocity if there is a collision below the feet
            // (player is on ground)

            // potential bugs:
            // if a player falls and hits the side of a tile but not the top of it no gravity will be added despite still falling

            int tileX = getTileX();
            int tileY = getTileY();
            int startX = tileX - MAX_VELOCITY;
            int startY = tileY - MAX_VELOCITY;
            int endX = tileX + MAX_VELOCITY;
            int endY = tileY + MAX_VELOCITY;
            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    Tile tile = latice.get(x, y);
                    if (!tile.getMaterial().isSolid()) continue;

                    hitbox.resolve(new Rectangle(new ImmoveablePoint(x, y), 1, 1));
                }
            }
        }
        {
            MoveablePoint effectiveVelocity = velocity.multiply(ticks);
            getPos().addSet(effectiveVelocity);
        }

        if (isOutside()) {
            double newTemperature = getTile().transitionTemperature(temperature, ticks);
            this.warmingUp = newTemperature > temperature;
            this.temperature = newTemperature;
        }
        if (isTooCold() && !isWarmingUp()) {
            this.health -= ticks;
        }
        if (food < 1) {
            health -= HEALTH_PER_STARVE;
        } else food -= ticks;
    }

    @Override
    public int getTileX() {
        return (int) getPos().getX();
    }

    @Override
    public int getTileY() {
        return (int) getPos().getY();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getCenterX() {
        return getX() + (hitbox.getWidth() * 0.5);
    }

    public double getCenterY() {
        return getY();
    }
}
