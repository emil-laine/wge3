package wge3.entity.object;

import wge3.world.MapObject;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;
    
    public abstract void use();
}
