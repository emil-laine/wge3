package wge3.entity.character;

import wge3.game.ai.AI;

public abstract class NonPlayer extends Creature {
    
    protected AI ai;
    protected int attackSpeed; // How many times the creature attacks in 5 seconds.
    
    public NonPlayer() {
        team = 1;
        ai = new AI(this);
        attackSpeed = 10;
        picksUpItems = false;
    }
    
    public void updateAI() {
        ai.update();
    }
    
    public int getAttackSpeed() {
        return attackSpeed;
    }
}
