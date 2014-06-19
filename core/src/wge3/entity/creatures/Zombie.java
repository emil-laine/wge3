package wge3.entity.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.character.NonPlayer;
import wge3.world.Tile;

public final class Zombie extends NonPlayer {

    public Zombie() {
        texture = new Texture(Gdx.files.internal("graphics/terrain.png"));
        sprite = new Sprite(texture, 6*Tile.size, 2*Tile.size, Tile.size, Tile.size);
        
        maxHP = 50;
        defense = 5;
    }
}
