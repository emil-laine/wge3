package wge3.game.entity.tilelayers;

import java.util.EnumSet;
import wge3.game.engine.constants.TilePropertyFlag;
import static wge3.game.engine.constants.TilePropertyFlag.BLOCKS_VISION;
import static wge3.game.engine.constants.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.game.engine.constants.TilePropertyFlag.COVERS_WHOLE_TILE;
import static wge3.game.engine.constants.TilePropertyFlag.IS_PASSABLE;
import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.tilelayers.mapobjects.Teleport;
import wge3.game.entity.tilelayers.mapobjects.Tree;

public abstract class MapObject extends TileLayer {
    
    protected int maxHP;
    protected int HP;
    protected int hardness;
    protected float shadowDepth;
    
    public MapObject() {
        // Default values:
        maxHP = 100;
        HP = maxHP;
        propertyFlags = EnumSet.of(IS_PASSABLE, BLOCKS_VISION, COVERS_WHOLE_TILE, CASTS_SHADOWS);
        hardness = 9;
    }

    public void dealDamage(int amount) {
        HP -= Math.max(amount - hardness, 0);
    }
    
    public boolean isDestroyed() {
        return HP <= 0;
    }

    public boolean hasDestroyedSprite() {
        return propertyFlags.contains(TilePropertyFlag.HAS_DESTROYED_SPRITE);
    }

    public boolean isSlime() {
        return this.getClass() == GreenSlime.class;
    }
    
    public boolean isTree() {
        return this.getClass() == Tree.class;
    }

    public boolean isTeleport() {
        return this.getClass() == Teleport.class;
    }
    
    public boolean coversWholeTile() {
        return propertyFlags.contains(TilePropertyFlag.COVERS_WHOLE_TILE);
    }
    
    public boolean castsShadows() {
        return propertyFlags.contains(TilePropertyFlag.CASTS_SHADOWS);
    }

    public float getShadowDepth() {
        return shadowDepth;
    }
}
