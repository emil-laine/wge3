/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static wge3.game.engine.utilities.Math.getDistance;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.items.Gun;

/**
 *
 * @author Chang
 */
public class RangedAttackTask extends AITask {
    
    private NonPlayer executor;
    private Creature target;
    private MoveTask subTask;
    private long timeOfLastAttack;
    
    public RangedAttackTask(NonPlayer executor, Creature target) {
        this.executor = executor;
        this.target = target;
        
        subTask = new MoveTask(executor, target.getTileUnder());
        timeOfLastAttack = millis();
    }
    
    @Override
    public void execute() {
        // Check if target has moved to a new tile:
        Tile targetTile = target.getTileUnder();
        
        if (!isWithinGunRange(target)) {
            subTask.setDestination(targetTile);
            return;
        }
        if (!executor.isFacing(target)) {
            subTask = new MoveTask(executor, target.getTileUnder());
            subTask.execute();
            return;
        }
        if (!subTask.isFinished()) subTask.execute();
        
        if (canAttack()) {
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
    
    public boolean isWithinGunRange(Creature enemy) {
        Gun gun = (Gun) executor.getSelectedItem();
        
        return getDistance(executor, enemy) / Tile.size <= gun.getRange();
    }
}
