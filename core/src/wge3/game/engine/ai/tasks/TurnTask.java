package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static java.lang.Math.abs;
import wge3.game.entity.creatures.Creature;

public final class TurnTask extends AITask {
    
    private Creature executor;
    private float targetDirection;
    private float diff;

    public TurnTask(Creature executor, float targetDirection) {
        this.executor = executor;
        this.targetDirection = targetDirection;
        
        diff = getDiff(executor.getDirection(), targetDirection);
    }
    
    public TurnTask(Creature executor, Creature target) {
        this.executor = executor;
        
        float destX = target.getX();
        float destY = target.getY();
        
        float dx = destX - executor.getX();
        float dy = destY - executor.getY();
        float angle = atan2(dy, dx);
        if (angle < 0) angle += PI2; /* angle = [0, PI2[ */
        diff = getDiff(executor.getDirection(), angle);
        
        this.targetDirection = angle;
    }

    public static float getDiff(float src, float dest) {
        float diff = dest - src;
        if (diff > PI) diff -= PI2;
        else if (diff < -PI) diff += PI2;
        return diff;
    }
    
    @Override
    public void execute() {
        if (diff > 0) {
            executor.turnLeft();
        } else if (diff < 0) {
            executor.turnRight();
        }
    }

    @Override
    public boolean isFinished() {
        return abs(getDiff(executor.getDirection(), targetDirection)) < PI/48;
    }
}
