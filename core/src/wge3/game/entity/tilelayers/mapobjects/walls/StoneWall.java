package wge3.game.entity.tilelayers.mapobjects.walls;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.mapobjects.Wall;
import wge3.game.entity.Tile;

public final class StoneWall extends Wall {

    public StoneWall(int type) {
        if (type < 0 || type > 2) throw new IllegalArgumentException();
        sprite = new Sprite(texture, 3*Tile.size + type*Tile.size, Tile.size, Tile.size, Tile.size);
    }

    @Override
    public void dealDamage(int amount) {
    }
}
