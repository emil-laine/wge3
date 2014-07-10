package wge3.game.entity.tilelayers.grounds;

import wge3.game.entity.tilelayers.Ground;

public final class Grass extends Ground {

    public Grass() {
        setSprite(0, 0);
        movementModifier = 15/16f;
    }

}
