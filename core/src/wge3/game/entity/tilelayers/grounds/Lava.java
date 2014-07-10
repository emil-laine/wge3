package wge3.game.entity.tilelayers.grounds;

import wge3.game.entity.tilelayers.Ground;

public final class Lava extends Ground {

    public Lava() {
        setSprite(3, 0);
        drainsHP = true;
        HPDrainAmount = 15;
        movementModifier = 0.5f;
    }
}
