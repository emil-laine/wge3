package wge3.entity.ground;

import wge3.entity.terrainelement.Ground;

public final class Water extends Ground {

    public Water() {
        super("graphics/water.png");
        
        affectsMovement = true;
        movementModifier = 0.7f;
    }
}
