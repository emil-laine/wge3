package wge3.game.entity.tilelayers.mapobjects;

import java.util.EnumSet;
import static wge3.game.engine.constants.TilePropertyFlag.BLOCKS_VISION;
import static wge3.game.engine.constants.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.game.engine.constants.TilePropertyFlag.COVERS_WHOLE_TILE;
import wge3.game.entity.tilelayers.MapObject;

public abstract class Wall extends MapObject {
    
    public Wall() {
        propertyFlags = EnumSet.of(BLOCKS_VISION, CASTS_SHADOWS, COVERS_WHOLE_TILE);
    }
}
