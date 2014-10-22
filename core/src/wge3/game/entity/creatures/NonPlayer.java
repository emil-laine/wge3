package wge3.game.entity.creatures;

import wge3.game.engine.ai.AI;
import static wge3.game.engine.constants.Team.MonsterTeam;

public abstract class NonPlayer extends Creature {
    
    protected AI ai;
    protected int attackSpeed; // How many times the creature attacks in 5 seconds.
    
    public NonPlayer() {
        team = MonsterTeam;
        ai = new AI(this);
        attackSpeed = 10;
    }
    
    public void updateAI() {
        ai.update();
    }
    
    public int getAttackSpeed() {
        return attackSpeed;
    }
}
