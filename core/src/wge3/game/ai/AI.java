package wge3.game.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import static wge3.game.PlayState.mStream;

public final class AI {

    protected NonPlayer creature; /* the controlled creature */
    protected AITask currentTask;

    public AI(NonPlayer creature) {
        this.creature = creature;
        currentTask = new WaitTask(0); /* dummy task */
    }
    
    public void update() {
        for (Creature dude : creature.getArea().getCreatures()) {
            if (dude.canBeSeenBy(creature) && dude.getTeam() != creature.getTeam()) {
                currentTask = new AttackTask(creature, dude);
                return;
            }
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean()) {
            currentTask = new MoveTask(creature, creature.getNewMovementDestination());
        } else {
            currentTask = new WaitTask(random(2000));
        }
    }
}
