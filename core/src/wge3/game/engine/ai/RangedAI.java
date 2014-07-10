/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.engine.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import java.util.concurrent.Executor;
import wge3.game.engine.ai.tasks.MeleeAttackTask;
import wge3.game.engine.ai.tasks.MoveTask;
import wge3.game.engine.ai.tasks.RangedAttackTask;
import wge3.game.engine.ai.tasks.WaitTask;
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
            currentTask = new MoveTask(NPC, NPC.getNewMovementDestination());
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }
    
    @Override
        public void checkForEnemies() {
        for (Creature dude : NPC.getEnemiesWithinFOV()) {
            // If dude is within range, attack:
            if (!NPC.getSelectedItem().isGun()) {
                currentTask = new MeleeAttackTask(NPC, dude);
                return;
            }
            if (isWithinGunRange(dude)) {
                currentTask = new RangedAttackTask(NPC, dude);
                return;
            }
        }
    }
    
        public boolean isWithinGunRange(Creature enemy) {
            Gun gun = (Gun) NPC.getSelectedItem();
            
            if (super.NPC.getDistanceTo(enemy) <= gun.getRange()) {
                return true;
            }
            
            return false;
        }
        
}
