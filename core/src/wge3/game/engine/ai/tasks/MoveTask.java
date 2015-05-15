package wge3.game.engine.ai.tasks;

import java.util.ArrayList;
import java.util.List;
import wge3.game.entity.creatures.Creature;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.Tile;
import static wge3.game.engine.utilities.Math.angle;
import static wge3.game.engine.utilities.Math.isInCenterOfATile;

public final class MoveTask extends AITask {
    
    private Creature executor;
    private TurnTask turnTask;
    private List<Tile> path;
    private int position;
    
    public MoveTask(Creature executor, Tile dest) {
        this.executor = executor;
        
        if (dest.isAnOKMoveDestinationFor(executor)) {
            path = new ArrayList(1);
            path.add(dest);
        } else {
            path = findPath(executor.getTileUnder(), dest);
            if (path == null) return;
        }
        
        position = 0;
        float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
        turnTask = new TurnTask(executor, angle);
    }
    
    @Override
    public boolean isFinished() {
        return path == null || (executor.getTileUnder().equals(getDestination()) && isInCenterOfATile(executor));
    }
    
    @Override
    public void execute() {
        if (!turnTask.isFinished()) {
            turnTask.execute();
            return;
        }
        
        if (executor.getTileUnder().equals(path.get(position)) && isInCenterOfATile(executor)) {
            position++;
            float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
            turnTask = new TurnTask(executor, angle);
        } else {
            executor.goForward();
        }
    }
    
    public Tile getDestination() {
        return path == null ? null : path.get(path.size()-1);
    }
    
    public void setDestination(Tile dest) {
        if (path != null) path.set(path.size()-1, dest);
    }
}
