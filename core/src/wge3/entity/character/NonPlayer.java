package wge3.entity.character;

import wge3.world.Area;

public class NonPlayer extends Creature {

    public NonPlayer() {
        super();
    }

    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }
}
