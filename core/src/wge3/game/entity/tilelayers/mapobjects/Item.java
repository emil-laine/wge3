package wge3.game.entity.tilelayers.mapobjects;

import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.items.Gun;
import wge3.game.entity.tilelayers.MapObject;

public abstract class Item extends MapObject {
    
    protected String name;
    protected int value;
    protected int defaultAmount;

    public Item() {
        // default values
        blocksVision = false;
        coversWholeTile = false;
        defaultAmount = 1;
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
    
    public boolean isGun() {
        return this.getClass().getSuperclass() == Gun.class;
    }
    
    public int getDefaultAmount() {
        return defaultAmount;
    }
}
