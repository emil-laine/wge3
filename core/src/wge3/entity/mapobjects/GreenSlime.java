package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.MapObject;
import wge3.world.Tile;

public class GreenSlime extends MapObject {

    public GreenSlime() {
        sprite = new Sprite(texture, Tile.size, 4*Tile.size, Tile.size, Tile.size);
        
        passable = false;
        blocksVision = false;
        HP = 1000;
        hardness = 50;
    }
}
