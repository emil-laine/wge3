package wge3.game.ai;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;

public final class AttackTask extends AITask {
    
    private NonPlayer executor;
    private Creature target;
    private MoveTask subTask;
    private long timeOfLastAttack;

    public AttackTask(NonPlayer executor, Creature target) {
        this.executor = executor;
        this.target = target;
        
        subTask = new MoveTask(executor, target.getTile());
        timeOfLastAttack = millis();
    }

    @Override
    public void execute() {
        // Check if target has moved to a new tile:
        if (subTask.getDestination() != target.getTile() && target.canBeSeenBy(executor)) {
            subTask.setDestination(target.getTile());
        }
        if (!subTask.isFinished()) subTask.execute();
        else if (target.getTile() == executor.getTile() && canAttack()) {
            executor.useItem();
            timeOfLastAttack = millis();
        }
    }

    @Override
    public boolean isFinished() {
        return target.isDead() || (subTask.isFinished() && !target.canBeSeenBy(executor));
    }
    
    public boolean canAttack() {
        return millis() - timeOfLastAttack > 5000/executor.getAttackSpeed();
    }
}
