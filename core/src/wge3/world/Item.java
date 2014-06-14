package wge3.world;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;
    
    public abstract void use();
}
