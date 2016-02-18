/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import static wge3.engine.PlayState.mStream;
import wge3.model.Tile;

public final class FusedBomb extends Bomb {
    
    private int damage;
    
    public FusedBomb() {
        setSprite(0, 2);
        range = 2;
        damage = 100;
        time = 2;
    }
    
    @Override
    public void explode() {
        mStream.addMessage("*EXPLOSION*");
        
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range)) {
                float distance = currentTile.getDistanceTo(x, y) / Tile.size;
                float intensity = 1f - Math.max(distance-1f, 0f) * (1f / range);
                // intensity = 1, when distance = [0,1].
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
        area.removeBomb(this);
    }
}
