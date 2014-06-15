package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Tile;

public final class BrickWall extends Wall {

    public BrickWall() {
        sprite = new Sprite(texture, 0, Tile.size, Tile.size, Tile.size);
    }
}
