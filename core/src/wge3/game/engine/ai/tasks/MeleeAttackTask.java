package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.Tile;

public final class MeleeAttackTask extends AITask {
    
    private NonPlayer executor;
    private Creature target;
    private MoveTask subTask;
    private long timeOfLastAttack;

    public MeleeAttackTask(NonPlayer executor, Creature target) {
        this.executor = executor;
        this.target = target;
        
        subTask = new MoveTask(executor, target.getTile());
        timeOfLastAttack = millis();
    }

    @Override
    public void execute() {
        // Check if target has moved to a new tile:
        Tile targetTile = target.getTile();
        if (subTask.getDestination() != targetTile && target.canBeSeenBy(executor) && targetTile.isAnOKMoveDestinationFor(executor)) {
            subTask.setDestination(targetTile);
        }
        if (!subTask.isFinished()) subTask.execute();
        else if (executor.isInSameTileAs(target) && canAttack()) {
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
