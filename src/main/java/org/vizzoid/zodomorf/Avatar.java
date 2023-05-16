package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.DynamicRectangle;
import org.vizzoid.utils.position.ImmoveablePoint;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Rectangle;
import org.vizzoid.zodomorf.engine.LaticeCamera;

public class Avatar implements LaticeCamera {

    public static final double HEALTH_PER_STARVE = 0.016666666666666666;
    public static final int MAX_VELOCITY = 5;
    private static final double GRAVITY = -0.05;

    private Planet planet;
    private double health = 100;
    private double food = 30000;
    private double temperature = 100;
    private boolean warmingUp = false;
    private final DynamicRectangle hitbox = new DynamicRectangle(new MoveablePoint(), 0.9, 1.9, new MoveablePoint());
    private boolean jumping;
    private boolean movingLeft, movingRight;
    private int miningTileHealth;
    private Tile mouseTile = Tile.EMPTY;
    private boolean clicking = false;

    public Avatar() {

    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
        Latice<Tile> latice = planet.getTileLatice();
        hitbox.getPos().set(latice.getWidth() * 0.5, 150);
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
        if (velocity.getX() == 0) {
            if (!(movingLeft && movingRight)) {
                if (movingLeft) velocity.setX(-0.5);
                if (movingRight) velocity.setX(0.5);
            }
        }
        {
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
        // potential bugs: if jumping into block may faze through as collision is not tested (Will not work before collision with current system because gravity is removed with collision
        if (jumping && velocity.getY() == 0) {
            velocity.setY(0.5);
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

        if (clicking) {
            if (--miningTileHealth < 0) {
                mouseTile.setMaterial(Material.EMPTY);
                mouseTile = Tile.EMPTY;
                miningTileHealth = 5;
            }
        } else miningTileHealth = 5;
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

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setJumping(boolean b) {
        this.jumping = b;
    }

    public double getTemperature() {
        return temperature;
    }

    public DynamicRectangle getHitbox() {
        return hitbox;
    }

    public int getMiningTileHealth() {
        return miningTileHealth;
    }

    public Tile getMouseTile() {
        return mouseTile;
    }

    public void setMouseTile(Tile mouseTile) {
        if (!mouseTile.isSolid()) mouseTile = Tile.EMPTY;
        this.mouseTile = mouseTile;
        miningTileHealth = 5;
    }

    public void setClicking(boolean clicking) {
        this.clicking = clicking;
    }
}
