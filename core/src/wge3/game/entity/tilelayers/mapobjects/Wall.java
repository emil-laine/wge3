package wge3.game.entity.tilelayers.mapobjects;

import wge3.game.engine.constants.TilePropertyFlag;
import wge3.game.entity.tilelayers.MapObject;

public abstract class Wall extends MapObject {

    public Wall() {
        propertyFlags.remove(TilePropertyFlag.IS_PASSABLE);
    }
}
