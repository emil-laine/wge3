/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import java.util.EnumSet;
import static wge3.model.TilePropertyFlag.BLOCKS_VISION;
import static wge3.model.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.model.TilePropertyFlag.COVERS_WHOLE_TILE;
import static wge3.model.TilePropertyFlag.IS_PASSABLE;
import wge3.model.objects.GreenSlime;
import wge3.model.objects.Teleport;
import wge3.model.objects.Tree;

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
    
    /** Deals damage to this object.
     *  @param amount how much damage to deal */
    public void dealDamage(int amount) {
        if (amount > 0) HP -= amount;
    }
    
    /** Returns whether this object has taken enough damage to be destroyed. */
    public boolean isDestroyed() {
        return HP <= 0;
    }
    
    /** Returns whether this object type has a separate graphical representation
     *  to be shown when it's destroyed. */
    public boolean hasDestroyedSprite() {
        return propertyFlags.contains(TilePropertyFlag.HAS_DESTROYED_SPRITE);
    }
    
    /** Returns whether this object is a GreenSlime. */
    public boolean isSlime() {
        return this.getClass() == GreenSlime.class;
    }
    
    /** Returns whether this object is a Tree. */
    public boolean isTree() {
        return this.getClass() == Tree.class;
    }
    
    /** Returns whether this object is a Teleport. */
    public boolean isTeleport() {
        return this.getClass() == Teleport.class;
    }
    
    /** Returns whether the graphical representation of this object fills every
     *  pixel of its tile, fully covering the ground beneath it. */
    public boolean coversWholeTile() {
        return propertyFlags.contains(TilePropertyFlag.COVERS_WHOLE_TILE);
    }
    
    /** Returns whether to draw shadows for this type of object. */
    public boolean castsShadows() {
        return propertyFlags.contains(TilePropertyFlag.CASTS_SHADOWS);
    }
    
    /** Returns the steepness of the lightness curve for the shadows cast by
     *  this object.
     *  @return in the range [0.0, 1.0]:
     *          1.0f - The shadow of this object is 100% the lightness on this
     *                 object, i.e. no visible shadows.
     *          0.0f - The shadow of this object is 0% the lightness on this
     *                 object, i.e. the shadow will be entirely black. */
    public float getShadowDepth() {
        return shadowDepth;
    }
}
