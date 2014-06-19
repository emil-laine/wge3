package wge3.entity.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.character.NonPlayer;
import wge3.world.Tile;

public class Zombie extends NonPlayer {

    public Zombie() {
        texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
        sprite = new Sprite(texture, 6*Tile.size, 2*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
        
        maxHP = 50;
        HP = 50;
        strength = 8;
        defense = 5;
    }
}
