package wge3.entity.mapobject;

import wge3.entity.terrainelement.MapObject;

public abstract class Wall extends MapObject {

    public Wall() {
        
        passable = false;
    }
}