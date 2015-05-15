package wge3.game.entity.creatures.npcs;

import wge3.game.engine.ai.RangedAI;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.items.guns.Handgun;

/**
 *
 * @author chang
 */
public final class Gunman extends NonPlayer{
    
    public Gunman() {
        setSprite(4, 3);
        HP.setMax(60);
        strength = 10;
        defense = 5;
        defaultSpeed = 70;
        currentSpeed = 70;
        inventory.addItem(new Handgun(), 60);
        this.changeItem();
        ai = new RangedAI(this);
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
