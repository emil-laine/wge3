package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Tile;

public final class Handgun extends Gun {
    
    public Handgun() {
        sprite = new Sprite(texture, Tile.size, 2*Tile.size, Tile.size, Tile.size);
        name = "handgun";
    }
}
