package wge3.game.entity.tilelayers;

import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.tilelayers.mapobjects.Tree;

public abstract class MapObject extends TileLayer {
    
    protected int maxHP;
    protected int HP;
    protected boolean hasDestroyedSprite;
    protected int hardness;
    protected boolean coversWholeTile;
    protected boolean castsShadows;
    protected float shadowDepth;
    
    public MapObject() {
        // Default values:
        maxHP = 100;
        HP = maxHP;
        passable = true;
        blocksVision = true;
        drainsHP = false;
        hardness = 9;
        coversWholeTile = true;
        castsShadows = false;
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

    public boolean isSlime() {
        return this.getClass() == GreenSlime.class;
    }
    
    public boolean isTree() {
        return this.getClass() == Tree.class;
    }
    
    public boolean coversWholeTile() {
        return coversWholeTile;
    }
    
    public boolean castsShadows() {
        return castsShadows;
    }

    public float getShadowDepth() {
        return shadowDepth;
    }
}