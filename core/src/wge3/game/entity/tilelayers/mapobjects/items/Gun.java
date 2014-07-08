
package wge3.game.entity.tilelayers.mapobjects.items;

import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.entity.Tile;

public abstract class Gun extends Item {
    protected int range;
    protected int damage;
    protected int lineOfFireWidth;
    
    public Gun() {
        defaultAmount = 20;
        lineOfFireWidth = Tile.size / 2;
    }
    
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

    public int getLineOfFireWidth() {
        return lineOfFireWidth;
    }
}
     
