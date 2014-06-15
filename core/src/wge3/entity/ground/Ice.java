package wge3.entity.ground;

import wge3.entity.terrainelement.Ground;

public final class Ice extends Ground {

    public Ice() {
        super(null);
        
        affectsMovement = true;
        movementModifier = 1.5f;
    }
}
