/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.PI;
import wge3.engine.util.Debug;
import static wge3.model.Team.MONSTER_TEAM;
import wge3.model.ai.AI;
import wge3.model.ai.RangedAI;
import wge3.model.ai.ThiefAI;

public final class NonPlayer extends Creature {
    
    private AI ai;
    private int attackSpeed; // How many times the creature attacks in 5 seconds.
    private float FOV;
    
    public NonPlayer(String type) {
        super(type);
        
        team = MONSTER_TEAM;
        ai = getAI(type);
        attackSpeed = 10;
        FOV = PI;
    }
    
    public void updateAI() {
        ai.update();
    }
    
    protected void setAI(AI newAI) {
        ai = newAI;
    }
    
    public int getAttackSpeed() {
        return attackSpeed;
    }
    
    public float getFOV() {
        return FOV;
    }
    
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        
        if (Debug.aiDebug) {
            ai.draw(batch);
        }
    }
    
    private AI getAI(String type) {
        switch (getCfg().getString(type, "behavior")) {
            case "meleeAttack": return new AI(this);
            case "rangedAttack": return new RangedAI(this);
            case "thief": return new ThiefAI(this);
            default: throw new UnsupportedOperationException("Unknown AI");
        }
    }
}
