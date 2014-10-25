package wge3.game.entity.tilelayers.mapobjects;

import wge3.game.engine.constants.TilePropertyFlag;
import wge3.game.entity.tilelayers.MapObject;

public class Tree extends MapObject {

    public Tree() {
        setSprite(6, 1);
        propertyFlags.remove(TilePropertyFlag.IS_PASSABLE);
        propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
        propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
        propertyFlags.add(TilePropertyFlag.CASTS_SHADOWS);
        shadowDepth = 0.75f;
    }
}
