package wge3.entity.object;

import wge3.world.MapObject;

public abstract class Wall extends MapObject {

    public Wall() {
        passable = false;
    }
}
