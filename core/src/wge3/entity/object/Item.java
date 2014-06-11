package wge3.entity.object;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;
    
    public abstract void use();
}
