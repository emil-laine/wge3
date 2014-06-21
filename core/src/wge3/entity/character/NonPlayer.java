package wge3.entity.character;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static wge3.game.PlayState.mStream;
import wge3.game.ai.AITask;
import wge3.game.ai.AttackTask;
import wge3.game.ai.MoveTask;
import wge3.game.ai.WaitTask;
import wge3.world.Tile;

public abstract class NonPlayer extends Creature {
    
    protected AITask currentTask;
    
    public NonPlayer() {
        team = 1;
        currentTask = new WaitTask(1000);
    }
    
    public void updateAI() {
        /*for (Creature creature : area.getCreatures()) {
            if (creature.canBeSeenBy(this) && creature.getTeam() != this.getTeam()) {
                currentTask = new AttackTask(this, creature);
                return;
            }
        }*/
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean()) {
            currentTask = new MoveTask(this, getNewMovementDestination());
        } else {
            currentTask = new WaitTask(2000);
        }
    }
}
