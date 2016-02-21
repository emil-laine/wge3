/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static wge3.engine.util.Math.getDistance2;
import wge3.model.NonPlayer;
import wge3.model.objects.Item;

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
        
        if (!getCurrentTask().isFinished()) {
            getCurrentTask().execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            setCurrentTask(new MoveTask(getNPC(), getNewMovementDestination(getNPC())));
        } else {
            setCurrentTask(new WaitTask(random(3000)));
        }
    }
    
    @Override
    public void checkForEnemies() {
        if (getNPC().getSelectedItem().isRangedWeapon()) {
            Item gun = getNPC().getSelectedItem();
            int range = gun.getIntAttribute("range");
            int gunRange2 = range * range;
            
            getNPC().getEnemiesWithinFOV()
                    .parallelStream()
                    .filter(x -> getDistance2(getNPC(), x) <= gunRange2)
                    .findAny()
                    .ifPresent(x -> setCurrentTask(new RangedAttackTask(getNPC(), x)));
            
//            for (Creature dude : NPC.getEnemiesWithinFOV()) {
//                if (NPC.getDistance2To(dude) <= gunRange2) {
//                    currentTask = new RangedAttackTask(NPC, dude);
//                    return;
//                }
//            }
        } else {
            
            getNPC().getEnemiesWithinFOV()
                    .parallelStream()
                    .findAny()
                    .ifPresent(x -> setCurrentTask(new MeleeAttackTask(getNPC(), x)));
            
//            for (Creature dude : NPC.getEnemiesWithinFOV()) {
//                currentTask = new MeleeAttackTask(NPC, dude);
//                return;
//            }
        }
    }
}
