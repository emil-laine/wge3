package wge3.entity.terrainelements;

public abstract class MapObject extends TerrainElement {
    
    protected int HP;
    protected boolean hasDestroyedSprite;
    
    public MapObject() {
        
        // Default values:
        passable = true;
        blocksVision = true;
        drainsHealth = false;
    }

    public void dealDamage(int amount) {
        HP -= amount;
    }
    
    public boolean isDestroyed() {
        return HP <= 0;
    }

    public boolean hasDestroyedSprite() {
        return hasDestroyedSprite;
    }
}
