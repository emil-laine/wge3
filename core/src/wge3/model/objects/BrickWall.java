/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.objects;

import java.util.EnumSet;
import wge3.model.TilePropertyFlag;
import static wge3.model.TilePropertyFlag.BLOCKS_VISION;
import static wge3.model.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.model.TilePropertyFlag.COVERS_WHOLE_TILE;
import static wge3.model.TilePropertyFlag.HAS_DESTROYED_SPRITE;
import wge3.model.Tile;

public final class BrickWall extends Wall {
    
    public BrickWall() {
        setSprite(0, 1);
        propertyFlags = EnumSet.of(HAS_DESTROYED_SPRITE, BLOCKS_VISION, CASTS_SHADOWS, COVERS_WHOLE_TILE);
        maxHP = 150;
        HP = 150;
        hardness = 8;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
        
        if (isDestroyed()) {
            // show destroyed sprite
            getSprite().setRegion(2*Tile.size, Tile.size, Tile.size, Tile.size);
            propertyFlags.add(TilePropertyFlag.IS_PASSABLE);
            propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
            propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
            movementModifier = 0.7f;
        } else if (HP < maxHP/2) {
            // show damaged sprite
            getSprite().setRegion(Tile.size, Tile.size, Tile.size, Tile.size);
            propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
            propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
        }
    }
}
