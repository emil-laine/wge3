/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import wge3.model.objects.Item;
import wge3.model.Tile;

public abstract class Gun extends Item {
    protected int range;
    protected int damage;
    protected int lineOfFireWidth;
    
    public Gun() {
        defaultAmount = 20;
        lineOfFireWidth = Tile.size / 2;
    }
    
    public void setRange(int range) {
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public int getLineOfFireWidth() {
        return lineOfFireWidth;
    }
}
     
