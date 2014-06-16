package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.Item;
import wge3.world.Tile;

public final class Bomb extends Item {
    
    public Bomb() {
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        name = "bomb";
    }

    @Override
    public void use() {
        
    }
}
