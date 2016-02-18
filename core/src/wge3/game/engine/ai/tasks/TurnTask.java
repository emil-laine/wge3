/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.math.MathUtils.PI;
import static java.lang.Math.abs;
import static wge3.game.engine.utilities.Math.angle;
import static wge3.game.engine.utilities.Math.getDiff;
import wge3.game.entity.Tile;
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
        this(executor, angle(executor.getX(), executor.getY(), target.getX(), target.getY()));
    }
    
    public TurnTask(Creature executor, Tile target) {
        this(executor, angle(executor.getX(), executor.getY(), target.getMiddleX(), target.getMiddleY()));
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
