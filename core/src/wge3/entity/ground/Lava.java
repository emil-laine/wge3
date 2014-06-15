package wge3.entity.ground;

import wge3.entity.terrainelement.Ground;

public final class Lava extends Ground {

    public Lava() {
        super(null);
        
        drainsHealth = true;
        affectsMovement = true;
    }
}
