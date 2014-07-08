package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static java.lang.Math.abs;
import wge3.game.entity.creatures.Creature;
import static wge3.game.engine.ai.tasks.TurnTask.getDiff;
import wge3.game.entity.Tile;

public final class MoveTask extends AITask {
    
    private Creature executor;
    private Tile dest;
    private TurnTask subTask;

    public MoveTask(Creature executor, Tile dest) {
        this.executor = executor;
        this.dest = dest;
        
        int destX = dest.getX() * Tile.size + Tile.size/2;
        int destY = dest.getY() * Tile.size + Tile.size/2;
        float dx = destX - executor.getX();
        float dy = destY - executor.getY();
        float angle = atan2(dy, dx);
        if (angle < 0) angle += PI2; /* angle = [0, PI2[ */
        subTask = new TurnTask(executor, angle);
    }

    @Override
    public boolean isFinished() {
        return executor.getTile().equals(dest);
    }

    @Override
    public void execute() {
        if (!subTask.isFinished()) subTask.execute();
        else if (!this.isFinished()) {

            int destX = dest.getX() * Tile.size + Tile.size/2;
            int destY = dest.getY() * Tile.size + Tile.size/2;
            float dx = destX - executor.getX();
            float dy = destY - executor.getY();
            float angle = atan2(dy, dx);
            if (angle < 0) angle += PI2; /* angle = [0, PI2[ */
            
            if (abs(getDiff(executor.getDirection(), angle)) > PI/48) {
                subTask = new TurnTask(executor, angle);
            } else {
                executor.goForward();
            }
        }
    }

    public Tile getDestination() {
        return dest;
    }

    public void setDestination(Tile dest) {
        this.dest = dest;
    }
}
