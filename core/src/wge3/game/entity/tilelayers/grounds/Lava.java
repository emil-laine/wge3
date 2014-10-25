package wge3.game.entity.tilelayers.grounds;

import wge3.game.engine.constants.TilePropertyFlag;
import wge3.game.entity.tilelayers.Ground;

public final class Lava extends Ground {

    public Lava() {
        setSprite(3, 0);
        propertyFlags.add(TilePropertyFlag.DRAINS_HP);
        HPDrainAmount = 15;
        movementModifier = 0.5f;
    }
}
