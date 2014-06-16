package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Tile;

public final class SteelWall extends Wall {

    public SteelWall(int type) {
        if (type < 0 || type > 2) throw new IllegalArgumentException();
        sprite = new Sprite(texture, Tile.size, 3*Tile.size + type*Tile.size, Tile.size, Tile.size);
    }

    @Override
    public void dealDamage(int amount) {
    }
}
