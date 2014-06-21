package wge3.game.ai;

import wge3.entity.character.Creature;

public final class AttackTask extends AITask {
    
    private Creature executor;
    private Creature target;
    private MoveTask subTask;

    public AttackTask(Creature executor, Creature target) {
        this.executor = executor;
        this.target = target;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return !target.canBeSeenBy(executor) || target.isDead();
    }
    
    
}
