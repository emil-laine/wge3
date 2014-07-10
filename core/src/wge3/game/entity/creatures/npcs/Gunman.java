/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.entity.creatures.npcs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.engine.ai.RangedAI;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.tilelayers.mapobjects.items.guns.Handgun;

/**
 *
 * @author chang
 */
public class Gunman extends NonPlayer{
    
    public Gunman() {
        
        sprite = new Sprite(texture, 4*Tile.size, 3*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
        
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
