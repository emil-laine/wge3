package wge3.entity.ground;

import wge3.entity.terrainelement.Ground;

public final class Grass extends Ground {

    public Grass() {
        super("graphics/grass.png");
        
        affectsMovement = true;
        movementModifier = 0.9f;
    }

}
