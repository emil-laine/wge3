package wge3.game.engine.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import java.util.List;
import wge3.game.engine.ai.tasks.*;
import wge3.game.engine.utilities.pathfinding.PathFinder;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.creatures.npcs.Thief;

/**
 *
 * @author chang
 */


//The thief is a coward: he only acts when buddies are around. He closes in on his enemy FAST and takes an object he is carrying. 
//He then runs away. If the player can catch the thief and kill him, he can recover his items. The thief will do anything he can to avoid capture.
public class ThiefAI extends AI {

    private int cowardLevel; //how many friendlies should be nearby for the thief to become active
    private boolean itemStolen;
    
    
    public ThiefAI(Thief creature) {
        super(creature);
        cowardLevel = 4; 
        itemStolen = false;
    }
    
    public void setCowardLevel(int newLevel) {
        if (newLevel > 0) {
            cowardLevel = newLevel;
        }
    }
    
    public void update() {
        if (!isStealing()) {
            checkForEnemies();
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(NPC, NPC.getNewMovementDestination());
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }
    
        @Override
        public void checkForEnemies() {
        for (Creature dude : NPC.getEnemiesWithinFOV()) {
            if (!enoughFriendliesNearby() || !NPC.getInventory().getItems().isEmpty() || dude.getInventory().getItems().isEmpty()) {
                currentTask = new MultipleMoveTask(NPC, whereToRun(dude));
                return;
            }
            currentTask = new StealTask((Thief) NPC, dude);
            
        }
    }
    
    
    public boolean enoughFriendliesNearby() {
        return this.NPC.getFriendliesWithinFOV().size() >= cowardLevel;
    }
    
    public boolean isStealing() {
        return currentTask.getClass() == StealTask.class;
    }
    
    //plots a place to run away from enemy
    private List<Tile> whereToRun(Creature enemy) {
        
        
        return findPath(enemy.getArea().getTiles().get(3), NPC.getTile());
    }
}
