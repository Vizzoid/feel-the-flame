package org.vizzoid.zodomorf.entity.ai;

import org.vizzoid.utils.Optional;
import org.vizzoid.utils.position.MoveablePoint;
import org.vizzoid.utils.position.Point;
import org.vizzoid.zodomorf.entity.Animal;
import org.vizzoid.zodomorf.entity.path.Direction;
import org.vizzoid.zodomorf.entity.path.PlayerPathfinder;
import org.vizzoid.zodomorf.tile.Tile;

import java.util.Random;

public class AnimalAI {

    private final Animal animal;
    private Optional<Point> target;
    private int untilIdleWalk;
    private final PlayerPathfinder pathfinder;

    public AnimalAI(Animal animal) {
        this.animal = animal;
        resetIdleWalk();
        this.pathfinder = new PlayerPathfinder(getDirections(), animal);
    }

    protected Direction[] getDirections() {
        return new Direction[] {Direction.RIGHT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.LEFT, Direction.UP_LEFT, Direction.DOWN_LEFT};
    }

    public void resetIdleWalk() {
        untilIdleWalk = animal.getPlanet().getRandom().nextInt(60, 100);
    }

    public void tick(long ticks) {
        pathfinder.tick(ticks);
        boolean isDone = !pathfinder.getPath().isEmpty();
        if (animal.shouldFindFood()) {

            if (isDone) {
                animal.eat(animal.getPlanet().getTileLatice().get(target.getValue()));
                target = Optional.empty();
                return;
            }
            Tile animalTile = animal.getTile();
            for (int x = -5; x <= 5; x++) {
                for (int y = -5; y <= 5; y++) {
                    Tile tile = animalTile.relative(x, y);
                    if (!animal.canEat(tile)) continue;
                    target = Optional.of(tile.getPos());
                    return;
                }
            }
            return;
        }
        if (isDone) {
            resetIdleWalk();
            target = Optional.empty();
            return;
        }
        if (untilIdleWalk < 0) {

            Random random = animal.getPlanet().getRandom();
            MoveablePoint pos = animal.getPos().copyMoveable();
            pos.move(random.nextInt(7) - 3, random.nextInt(7) - 3);
            target = Optional.of(pos);
            return;
        }
        untilIdleWalk -= ticks;
    }

    public Optional<Point> getTarget() {
        return target;
    }
}

