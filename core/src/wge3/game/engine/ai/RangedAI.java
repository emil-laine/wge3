/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.engine.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import wge3.game.engine.ai.tasks.MeleeAttackTask;
import wge3.game.engine.ai.tasks.MoveTask;
import wge3.game.engine.ai.tasks.RangedAttackTask;
import wge3.game.engine.ai.tasks.WaitTask;
import static wge3.game.engine.utilities.Math.getDistance2;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.items.Gun;

/**
 *
 * @author chang
 */
public class RangedAI extends AI {

    public RangedAI(NonPlayer creature) {
        super(creature);
    }
    
    @Override
    public void update() {
        if (!isAttacking()) {
            checkForEnemies();
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(NPC, getNewMovementDestination(NPC));
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }
    
    @Override
    public void checkForEnemies() {
        if (NPC.getSelectedItem().isGun()) {
            Gun gun = (Gun) NPC.getSelectedItem();
            int gunRange2 = gun.getRange() * gun.getRange();
            for (Creature dude : NPC.getEnemiesWithinFOV()) {
                if (getDistance2(NPC, dude) <= gunRange2) {
                    currentTask = new RangedAttackTask(NPC, dude);
                    return;
                }
            }
        } else {
            for (Creature dude : NPC.getEnemiesWithinFOV()) {
                currentTask = new MeleeAttackTask(NPC, dude);
                return;
            }
        }
    }
}
