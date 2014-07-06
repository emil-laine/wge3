package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.MapObject;
import wge3.world.Tile;

public class Tree extends MapObject {

    public Tree() {
        sprite = new Sprite(texture, 6*Tile.size, Tile.size, Tile.size, Tile.size);
        
        passable = false;
        blocksVision = false;
        coversWholeTile = false;
        castsShadows = true;
        shadowDepth = 0.75f;
    }
}
