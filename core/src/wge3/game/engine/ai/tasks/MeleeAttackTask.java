package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.Tile;

public final class MeleeAttackTask extends AITask {
    
    private NonPlayer executor;
    private Creature target;
    private MoveTask moveTask;
    private long timeOfLastAttack;
    private long timeOfLastPathCalculation;

    public MeleeAttackTask(NonPlayer executor, Creature target) {
        this.executor = executor;
        this.target = target;
        
        moveTask = new MoveTask(executor, target.getTileUnder());
        timeOfLastAttack = millis();
        timeOfLastPathCalculation = millis();
    }

    @Override
    public void execute() {
        // Check if target has moved to a new tile:
        Tile targetTile = target.getTileUnder();
        if (canCalculatePath()
                && moveTask.getDestination() != targetTile
                && target.canBeSeenBy(executor)
                && targetTile.isGoodMoveDest()) {
            moveTask = new MoveTask(executor, targetTile);
            timeOfLastPathCalculation = millis();
        }
        if (!moveTask.isFinished()) moveTask.execute();
        else if (executor.isInSameTileAs(target) && canAttack()) {
            executor.useItem();
            timeOfLastAttack = millis();
        }
    }

    @Override
    public boolean isFinished() {
        return target.isDead() || (moveTask.isFinished() && !target.canBeSeenBy(executor));
    }
    
    public boolean canAttack() {
        return millis() - timeOfLastAttack > 5000/executor.getAttackSpeed();
    }
    
    public boolean canCalculatePath() {
        return millis() - timeOfLastPathCalculation > 500;
    }
}
