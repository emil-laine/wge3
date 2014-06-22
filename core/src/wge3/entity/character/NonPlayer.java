package wge3.entity.character;

import wge3.game.ai.AI;

public abstract class NonPlayer extends Creature {
    
    protected AI ai;
    
    public NonPlayer() {
        team = 1;
        ai = new AI(this);
    }
    
    public void updateAI() {
        ai.update();
    }
}
