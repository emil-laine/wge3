package wge3.entity.terrainelements;

public abstract class MapObject extends TerrainElement {
    
    protected int maxHP;
    protected int HP;
    protected boolean hasDestroyedSprite;
    
    public MapObject() {
        
        // Default values:
        maxHP = 50;
        HP = maxHP;
        passable = true;
        blocksVision = true;
        drainsHP = false;
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
