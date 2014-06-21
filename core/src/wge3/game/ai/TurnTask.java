package wge3.game.ai;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static java.lang.Math.abs;
import wge3.entity.character.Creature;
import static wge3.game.PlayState.mStream;

public final class TurnTask extends AITask {
    
    private Creature executor;
    private float targetDirection;
    private float diff;

    public TurnTask(Creature executor, float targetDirection) {
        this.executor = executor;
        this.targetDirection = targetDirection;
        
        diff = getDiff(executor.getDirection(), targetDirection);
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
