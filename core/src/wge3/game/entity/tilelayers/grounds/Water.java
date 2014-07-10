package wge3.game.entity.tilelayers.grounds;

import wge3.game.entity.tilelayers.Ground;

public final class Water extends Ground {

    public Water() {
        setSprite(2, 0);
        movementModifier = 0.5f;
    }
}
