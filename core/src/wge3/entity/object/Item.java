package wge3.entity.object;

import wge3.world.Area;

public abstract class Item extends GameObject {
    
    protected Area area;
    protected String name;
    protected int value;
    
    public void pickUp() {
        
    }
    
    public abstract void use(Area area, float x, float y);
}
