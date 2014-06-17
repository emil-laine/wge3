package wge3.entity.terrainelements;

public abstract class MapObject extends TerrainElement {
    
    protected int maxHP;
    protected int HP;
    protected boolean hasDestroyedSprite;
    protected int hardness;
    
    public MapObject() {
        
        // Default values:
        maxHP = 100;
        HP = maxHP;
        passable = true;
        blocksVision = true;
        drainsHP = false;
        hardness = 9;
    }

    public void dealDamage(int amount) {
        HP -= Math.max(amount - hardness, 0);
    }
    
    public boolean isDestroyed() {
        return HP <= 0;
    }

    public boolean hasDestroyedSprite() {
        return hasDestroyedSprite;
    }
}
