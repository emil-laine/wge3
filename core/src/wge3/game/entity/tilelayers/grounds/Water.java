package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

public final class Water extends Ground {

    public Water() {
        sprite = new Sprite(texture, 2*Tile.size, 0, Tile.size, Tile.size);
        
        movementModifier = 0.5f;
    }
}
