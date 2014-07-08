package wge3.game.entity.creatures.npcs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.Tile;

public class Zombie extends NonPlayer {

    public Zombie() {
        sprite = new Sprite(texture, 6*Tile.size, 2*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
        
        name = "zombie";
        maxHP = 50;
        HP = 50;
        strength = 10;
        defense = 5;
        defaultSpeed = 30;
        currentSpeed = 30;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
