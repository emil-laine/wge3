package wge3.entity.ground;

import wge3.entity.terrainelements.Ground;

public final class Lava extends Ground {

    public Lava() {
        
        drainsHealth = true;
        movementModifier = 0.7f;
    }
}
