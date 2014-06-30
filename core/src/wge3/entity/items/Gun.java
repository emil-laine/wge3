
package wge3.entity.items;

import wge3.entity.character.Creature;
import wge3.entity.terrainelements.Item;
import wge3.world.Tile;

public abstract class Gun extends Item {
    private int range;
    private int damage;
    
    public Gun() {}
    
    public void setRange(int range) {
        this.range = range;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
    
    
    
    @Override
    public void use(Creature user) {
        float angle = user.getDirection();
        
        
        int tileX = getX()*Tile.size + Tile.size/2;
        int tileY = getY()*Tile.size + Tile.size/2;
        float dx = user.getX() - tileX;
        float dy = user.getY() - tileY;
        
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
        distance /= Tile.size;
        
        for (int i = 1; i <= distance; i++) {
            Tile currentTile = user.getArea().getTileAt(tileX + i*(dx/distance), tileY + i*(dy/distance));
            if (currentTile.hasCreature()) {
                currentTile.getCreatures().get(0).dealDamage(getDamage());
                break;
            }
            else if (!currentTile.isPassable() && currentTile.blocksVision() ) {
                break;
            }
        }
    }
}
     
