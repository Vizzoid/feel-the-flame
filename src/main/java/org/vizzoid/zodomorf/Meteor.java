package org.vizzoid.zodomorf;

import org.vizzoid.utils.position.Point;
import org.vizzoid.utils.position.Rectangle;
import org.vizzoid.utils.position.*;
import org.vizzoid.zodomorf.engine.Images;
import org.vizzoid.zodomorf.tile.DebrisTile;
import org.vizzoid.zodomorf.tile.Material;
import org.vizzoid.zodomorf.tile.Tile;

import java.awt.*;

public class Meteor implements Entity {

    private final DynamicRectangle hitbox = new DynamicRectangle(Point.EMPTY, 1, 1, Point.EMPTY);
    private final Planet planet;

    public Meteor(Planet planet) {
        this(planet, planet.randomX());
    }

    public Meteor(Planet planet, int x) {
        this.planet = planet;
        int y = planet.getHighestY(x);

        int startX = planet.randomX();
        int startY = planet.getHeight() - 3;
        hitbox.getPos().set(startX, startY);
        MoveablePoint velocity = hitbox.getVelocity();
        velocity.set(x - startX, y - startY);
        velocity.multiplySet(3 / velocity.length());
    }

    public void tick(long ticks) {
        Latice<Tile> latice = planet.getTileLatice();
        {
            int tileX = (int) getPos().getX();
            int tileY = (int) getPos().getY();
            int startX = tileX - 5;
            int startY = tileY - 5;
            int endX = tileX + 5;
            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= tileY; y++) {
                    Tile tile = latice.get(x, y);
                    if (!tile.getMaterial().isSolid()) continue;

                    Collision collision = hitbox.intersectsDynamic(new Rectangle(new ImmoveablePoint(x, y), 1, 1));
                    if (!collision.is()) continue;
                    planet.removeEntity(this);
                    if (tile.isBoundary()) return;
                    Point point = collision.getHitPoint();
                    int impactX = (int) point.getX();
                    int impactY = (int) point.getY() + 1;

                    meteorTile(latice.get(impactX, impactY));
                    meteorTile(latice.get(impactX + 1, impactY));
                    meteorTile(latice.get(impactX - 1, impactY));
                    meteorTile(latice.get(impactX, impactY + 1));
                    meteorTile(latice.get(impactX, impactY - 1));

                    return;
                }
            }
        }

        hitbox.move(ticks);
    }

    public void meteorTile(Tile tile) {
        if (tile.isSolid()) return;
        tile.setMaterial(Material.DEBRIS, false, false);
        tile.setBehavior(new DebrisTile(tile));
    }

    @Override
    public Point getPos() {
        return hitbox.getPos();
    }

    @Override
    public Image getImage() {
        return Images.IMETEOR;
    }
}