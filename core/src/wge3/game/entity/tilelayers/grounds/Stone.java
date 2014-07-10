package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

public final class Stone extends Ground {

    public Stone() {
        sprite = new Sprite(texture, 4*Tile.size, 2*Tile.size,Tile.size, Tile.size);
    }

}
