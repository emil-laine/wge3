package wge3.entity.terrainelements;

import wge3.entity.character.Creature;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;

    public Item() {
        // default values
        blocksVision = false;
        coversWholeTile = false;
    }

    public String getName() {
        return name;
    }
    
    public abstract void use(Creature user);

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }
}
