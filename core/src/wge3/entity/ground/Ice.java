package wge3.entity.ground;

import wge3.entity.terrainelements.Ground;

public final class Ice extends Ground {

    public Ice() {
        
        affectsMovement = true;
        movementModifier = 1.5f;
    }
}
