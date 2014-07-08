package wge3.game.entity.tilelayers.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.MapObject;
import wge3.game.entity.Tile;

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
