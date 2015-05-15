package wge3.game.entity.tilelayers.mapobjects;

import java.util.EnumSet;
import static wge3.game.engine.constants.TilePropertyFlag.CASTS_SHADOWS;
import wge3.game.entity.tilelayers.MapObject;

public class Tree extends MapObject {
    
    public Tree() {
        setSprite(6, 1);
        propertyFlags = EnumSet.of(CASTS_SHADOWS);
        shadowDepth = 0.75f;
    }
}
