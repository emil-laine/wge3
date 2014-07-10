/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        name = "Gunman";
        HP.setMaximum(60);
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
