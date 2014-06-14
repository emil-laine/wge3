package wge3.world;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;

    public Item() {
        blocksVision = false;
    }

    public String getName() {
        return name;
    }
    
    public abstract void use();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
