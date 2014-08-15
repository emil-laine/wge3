package wge3.game.engine.ai.tasks;

import java.util.*;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;

/**
 *
 * @author chang
 */
public class MultipleMoveTask extends AITask {
    
    private MoveTask subtask;
    private List<Tile> tiles;
    private Creature executor;
    private int position;
    
    public MultipleMoveTask(Creature executor, List<Tile> tiles) {
        this.tiles = tiles;
        this.executor = executor;
        position = 0;
        subtask = new MoveTask(executor, tiles.get(position));
        
    }
    
    @Override
    public void execute() {
        if (!subtask.isFinished()) {
            subtask.execute();
        }
        else {
            position++;
            subtask = new MoveTask(executor, tiles.get(position));
        }
    }

    @Override
    public boolean isFinished() {
        return executor.getTile() == tiles.get(tiles.size()-1);
    }
    
}
