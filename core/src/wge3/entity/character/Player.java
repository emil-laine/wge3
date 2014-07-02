package wge3.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public final class Player extends Creature {
    
    public Player() {
        texture = new Texture(Gdx.files.internal("graphics/player.png"));
        sprite = new Sprite(texture);
        updateSpriteRotation();
        
        team = 0;
        size = 14;
        maxHP = 100;
        HP = maxHP;
        strength = 10;
        defense = 5;
        
        picksUpItems = true;
    }
}
