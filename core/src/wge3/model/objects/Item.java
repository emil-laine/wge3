/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.objects;

import java.util.EnumSet;
import wge3.model.TilePropertyFlag;
import wge3.model.Creature;
import wge3.model.items.Gun;
import wge3.model.MapObject;

public abstract class Item extends MapObject {
    
    protected String name;
    private int value;
    protected int defaultAmount;
    
    public Item() {
        // default values
        propertyFlags = EnumSet.of(TilePropertyFlag.IS_PASSABLE);
        defaultAmount = 1;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract void use(Creature user);
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }
    
    public boolean isGun() {
        return this.getClass().getSuperclass() == Gun.class;
    }
    
    public boolean isPotion() {
        return this.getClass().getSimpleName().toLowerCase().contains("potion");
    }
    
    public int getDefaultAmount() {
        return defaultAmount;
    }
}
