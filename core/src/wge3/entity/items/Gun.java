
package wge3.entity.items;

import wge3.entity.terrainelements.Item;

public abstract class Gun extends Item {
    private int range;
    private int damage;
    
    public Gun() {}
    
    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}
     
