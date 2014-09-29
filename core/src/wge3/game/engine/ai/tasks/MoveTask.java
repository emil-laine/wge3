package wge3.game.engine.ai.tasks;

import java.util.ArrayList;
import java.util.List;
import wge3.game.entity.creatures.Creature;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.Tile;
import static wge3.game.engine.utilities.Math.angle;

public final class MoveTask extends AITask {
    
    private Creature executor;
    private TurnTask turnTask;
    private List<Tile> path;
    private int position;

    public MoveTask(Creature executor, Tile dest) {
        this.executor = executor;
        
        if (dest.isAnOKMoveDestinationFor(executor)) {
            path = new ArrayList<Tile>(1);
            path.add(dest);
        } else {
            path = findPath(executor.getTile(), dest);
            if (path == null) return;
        }
        
        position = 0;
        float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
        turnTask = new TurnTask(executor, angle);
    }

    @Override
    public boolean isFinished() {
        return path == null || (executor.getTile().equals(getDestination()) && executor.isInCenterOfATile());
    }

    @Override
    public void execute() {
        if (!turnTask.isFinished()) {
            turnTask.execute();
            return;
        }
        
        if (!executor.getTile().equals(path.get(position)) || !executor.isInCenterOfATile()) {
            executor.goForward();
            return;
        }
        
        //if (executor.getTile().equals(getDestination())) executor.goForward();
        
        if (executor.getTile().equals(path.get(position)) && executor.isInCenterOfATile()) {
            position++;
            float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
            turnTask = new TurnTask(executor, angle);
        }
    }

    public Tile getDestination() {
        return path == null ? null : path.get(path.size()-1);
    }

    public void setDestination(Tile dest) {
        path.set(path.size()-1, dest);
    }
}
