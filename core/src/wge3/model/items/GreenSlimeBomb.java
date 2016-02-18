/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import wge3.model.items.Bomb;
import wge3.model.objects.GreenSlime;
import wge3.model.Tile;

public class GreenSlimeBomb extends Bomb {
    
    public GreenSlimeBomb() {
        setSprite(0, 4);
        range = 2;
        time = 2;
    }
    
    @Override
    public void explode() {
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range)
                    && !currentTile.hasObject()
                    && !currentTile.hasCreature()) {
                currentTile.setObject(new GreenSlime());
            }
        }
        area.removeBomb(this);
    }
}
