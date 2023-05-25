package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.*;
import org.vizzoid.zodomorf.building.Buildable;
import org.vizzoid.zodomorf.building.InsulatorSuit;
import org.vizzoid.zodomorf.engine.IntPoint;
import org.vizzoid.zodomorf.engine.LaticeCamera;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Avatar implements LaticeCamera, Serializable {

    @Serial
    private static final long serialVersionUID = -4087373531680187031L;
    public static final double HEALTH_PER_STARVE = 0.016666666666666666;
    public static final int MAX_VELOCITY = 5;
    public static final double GRAVITY = -0.03;
    public static final double TILE_DETECTION = 0.05;
    private static final double HORIZONTAL_SPEED = 0.3;
    public static final double HITBOX_WIDTH = 0.9, HITBOX_HEIGHT = 2.9;

    private transient Planet planet;
    private double health = 100;
    private double food = 30000;
    private double temperature = 100;
    private transient boolean warmingUp = false;
    private final DynamicRectangle hitbox = new DynamicRectangle(new MoveablePoint(), HITBOX_WIDTH, HITBOX_HEIGHT, new MoveablePoint());
    private transient boolean jumping;
    private transient boolean movingLeft, movingRight;
    private transient int miningTileHealth;
    private final IntPoint mouseTile = new IntPoint();
    private Tile miningTile;
    private transient boolean clicking = false;
    private final Map<String, Integer> storage = new HashMap<>();
    private transient Tile clickTile;
    private transient Buildable buildable = Buildable.EMPTY;
    private transient Material miningMiddleGround = Material.EMPTY;
    private transient InsulatorSuit suit = null;

    @Serial
    private void writeObject(ObjectOutputStream oos)
      throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(Game.getInstance().getPlanets().indexOf(planet));
        Point suitPos = suit == null ? new ImmoveablePoint(-1, -1) : suit.getPos();
        oos.writeObject(suitPos);
    }

    @Serial
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        Game.getInstance().setPlanet(ois.readInt());
        Point suitPos = (Point) ois.readObject();
        if (suitPos.getX() != -1) {
            suit = (InsulatorSuit) planet.getTileLatice()
                .get(suitPos.getX(), suitPos.getY())
                .getMiddleGroundBehavior();
        }
    }

    public Avatar() {

    }

    public InsulatorSuit getSuit() {
        return suit;
    }

    public void setSuit(InsulatorSuit suit) {
        this.suit = suit;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setWarmingUp(boolean warmingUp) {
        this.warmingUp = warmingUp;
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
        return suit != null && (temperature > 250 || temperature < 40);
    }

    public boolean isWarmingUp() {
        return warmingUp;
    }

    public Tile getTile() {
        return planet.getTileLatice().get(getTileX(), getTileY());
    }

    public boolean isOnGround() {
        Latice<Tile> latice = planet.getTileLatice();
        double width = hitbox.getWidth();
        int playerX = (int) hitbox.getPos().getX();
        int floorY = (int) (hitbox.getPos().getY() - TILE_DETECTION);
        for (int x = 0; x < width; x++) {
            if (latice.get(x + playerX, floorY).isSolid()) return true;
        }
        return false;
    }

    public boolean isBlockedLeft() {
        Latice<Tile> latice = planet.getTileLatice();
        double height = hitbox.getHeight();
        int wallX = (int) (hitbox.getPos().getX() - TILE_DETECTION);
        int playerY = (int) hitbox.getPos().getY();
        for (int y1 = 0; y1 < height; y1++) {
            int y = y1 + playerY;
            Tile tile = latice.get(wallX, y);
            if (!tile.getMaterial().isSolid()) continue;

            return hitbox.intersectsDynamic(new Rectangle(new ImmoveablePoint(wallX, y), 1, 1)).is();
        }
        return false;
    }

    public boolean isBlockedRight() {
        Latice<Tile> latice = planet.getTileLatice();
        double height = hitbox.getHeight();
        int wallX = (int) (hitbox.getPos().getX() + hitbox.getWidth() - TILE_DETECTION);
        int playerY = (int) hitbox.getPos().getY();
        for (int y1 = 0; y1 < height; y1++) {
            int y = y1 + playerY;
            Tile tile = latice.get(wallX, y);
            if (!tile.getMaterial().isSolid()) continue;

            return hitbox.intersectsDynamic(new Rectangle(new ImmoveablePoint(wallX, y), 1, 1)).is();
        }
        return false;
    }

    public void tick(long ticks) {
        Latice<Tile> latice = planet.getTileLatice();
        MoveablePoint velocity = getVelocity();
        MoveablePoint pos = getPos();
        boolean onGround = isOnGround();
        if (!onGround) velocity.moveY(GRAVITY * ticks);
        // potential bugs: if jumping into block may faze through as collision is not tested (Will not work before collision with current system because gravity is removed with collision
        if (jumping && velocity.getY() == 0) {
            velocity.setY(0.5);
        }

        if (Math.abs(velocity.getX()) < HORIZONTAL_SPEED) {
            if (!(movingLeft && movingRight)) {
                if (movingLeft) {
                    velocity.setX(-HORIZONTAL_SPEED);
                    if (isBlockedLeft()) {
                        velocity.setX(0);
                    }
                }
                if (movingRight) {
                    velocity.setX(HORIZONTAL_SPEED);
                    if (isBlockedRight()) {
                        velocity.setX(0);
                    }
                }
            }
        }
        // potential bugs: if jumping into block may faze through as collision is not tested (Will not work before collision with current system because gravity is removed with collision
        if (jumping && onGround) {
            velocity.setY(0.5);
        }

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
            int tileX = getTileX();
            int tileY = getTileY();
            int startX = tileX - MAX_VELOCITY;
            int startY = tileY - MAX_VELOCITY;
            int endX = tileX + MAX_VELOCITY;
            int endY = tileY + MAX_VELOCITY;
            for (int x = startX; x <= endX; x++) {
                for (int y = tileY; y <= endY; y++) {
                    Tile tile = latice.get(x, y);
                    if (!tile.getMaterial().isSolid()) continue;

                    hitbox.resolve(new Rectangle(new ImmoveablePoint(x, y), 1, 1));
                }
            }

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y < tileY; y++) {
                    Tile tile = latice.get(x, y);
                    if (!tile.getMaterial().isSolid()) continue;

                    Collision collision = hitbox.intersectsDynamic(new Rectangle(new ImmoveablePoint(x, y), 1, 1));
                    if (collision.is()) {
                        hitbox.resolve(collision);
                    }
                }
            }
        }

        hitbox.move(ticks);

        /*if (isOutside()) {
            double newTemperature = getTile().transitionTemperature(temperature, ticks);
            this.warmingUp = newTemperature > temperature;
            this.temperature = newTemperature;
        }*/
        if (isTooCold() && !isWarmingUp()) {
            this.health -= ticks;
        }
        if (food < 1) {
            health -= HEALTH_PER_STARVE;
        } else food -= ticks;

        if (clicking) {
            clickTile = getMouseTileRel();
            if (!buildable.isEmpty()) {
                if (!buildable.canPlace(clickTile)) return;
                buildable.place(clickTile);
                return;
            }

            if (clickTile.isSolid()) {
                if (!clickTile.equals(miningTile)) {
                    miningTileHealth = clickTile.getHealth();
                    miningTile = clickTile;
                }
                if ((miningTileHealth -= ticks) < 1) {
                    String material = clickTile.getMaterial().getKey();
                    int count = storage.computeIfAbsent(material, m -> 0);
                    storage.put(material, count + 1);

                    clickTile.setMaterial(Material.EMPTY);
                }
            } else if (clickTile.getMiddleGround().isSolid()) {
                if (!clickTile.equals(miningTile) || !clickTile.getMiddleGround().equals(miningMiddleGround)) {
                    miningTileHealth = clickTile.getMiddleGround().getHealth();
                    miningTile = clickTile;
                    miningMiddleGround = clickTile.getMiddleGround();
                }
                if ((miningTileHealth -= ticks) < 1) {
                    String material = clickTile.getMiddleGround().getKey();
                    int count = storage.computeIfAbsent(material, m -> 0);
                    storage.put(material, count + 1);

                    clickTile.setMiddleGround(Material.EMPTY);
                    miningMiddleGround = Material.EMPTY;
                }
            }
        }
    }

    public Tile getMouseTileRel() {
        MoveablePoint pos = getPos();
        return planet.getTileLatice().get(((int) pos.getX()) + mouseTile.getXInt(), ((int) pos.getY()) + mouseTile.getYInt());
    }

    public Buildable getBuildable() {
        return buildable;
    }

    public void setBuildable(Buildable buildable) {
        this.buildable = buildable;
    }

    public int getStorage(Material material) {
        return storage.getOrDefault(material.getKey(), 0);
    }

    public boolean spend(Material material, int amount) {
        int storage = getStorage(material);
        if (storage < amount) return false;
        setStorage(material, storage - amount);
        return true;
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

    public MoveablePoint getMouseTile() {
        return mouseTile;
    }

    public Tile getClickTile() {
        return clickTile;
    }

    public void setClicking(boolean clicking) {
        this.clicking = clicking;
    }

    public void setStorage(Material material, int i) {
        storage.put(material.getKey(), i);
    }
}
