/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.entity.tilelayers.mapobjects.items;

import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

/**
 *
 * @author Santeri
 */
public class SpeedPotion extends Item {
    private float speedBoostMultiplier;
    
    
    public SpeedPotion() {
        this.speedBoostMultiplier = 1.5f;
    }

    @Override
    public void use(Creature user) {
        user.setCurrentSpeed((int) (user.getCurrentSpeed() * speedBoostMultiplier));
    }
    
}
