/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import wge3.model.Creature;
import wge3.model.NonPlayer;
import wge3.model.Tile;

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
            moveTask.setDestination(targetTile);
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
    
    // For debugging
    void draw(Batch batch) {
        moveTask.draw(batch);
    }
}
