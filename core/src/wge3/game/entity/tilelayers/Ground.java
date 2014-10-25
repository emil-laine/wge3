package wge3.game.entity.tilelayers;

import wge3.game.engine.constants.TilePropertyFlag;

public abstract class Ground extends TileLayer {
    
    public Ground() {
        propertyFlags.add(TilePropertyFlag.IS_INDOORS);
    }

    public boolean isIndoors() {
        return propertyFlags.contains(TilePropertyFlag.IS_INDOORS);
    }
}
