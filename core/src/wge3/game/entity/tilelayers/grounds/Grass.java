package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

public final class Grass extends Ground {

    public Grass() {
        sprite = new Sprite(texture, 0, 0,Tile.size, Tile.size);
        
        movementModifier = 15/16f;
    }

}
