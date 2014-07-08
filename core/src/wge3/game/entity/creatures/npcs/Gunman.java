/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.entity.creatures.npcs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.NonPlayer;

/**
 *
 * @author chang
 */
public class Gunman extends NonPlayer{
    
    public Gunman() {
        
        sprite = new Sprite(texture, 6*Tile.size, 2*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
        
        name = "Gunman";
        maxHP = 60;
        HP = 60;
        strength = 10;
        defense = 5;
        defaultSpeed = 70;
        currentSpeed = 70;
        
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
