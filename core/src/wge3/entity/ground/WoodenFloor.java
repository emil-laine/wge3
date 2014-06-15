package wge3.entity.ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelement.Ground;
import wge3.world.Tile;

public final class WoodenFloor extends Ground {
    
    public WoodenFloor() {
        sprite = new Sprite(texture, Tile.size, 0, Tile.size, Tile.size);
    }
}
