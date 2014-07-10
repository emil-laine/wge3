package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

public final class WoodenFloor extends Ground {
    
    public WoodenFloor() {
        sprite = new Sprite(texture, Tile.size, 0, Tile.size, Tile.size);
        isIndoors = true;
    }
}
