/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import wge3.engine.util.Config;
import java.util.HashMap;
import java.util.Map;

/** Represents an item type, not an item instance. */
public class Item {
    
    private final String type;
    protected String name;
    private int value;
    protected int defaultAmount;
    private final boolean isRangedWeapon;
    public static final Config cfg = new Config("config/item.toml");
    private static final Map<String, Item> itemTypes = new HashMap();
    
    public static final Item get(String type) {
        if (!itemTypes.containsKey(type)) {
            itemTypes.put(type, new Item(type));
        }
        return itemTypes.get(type);
    }
    
    /** Use {@link #get(String)} to get an Item instance. */
    private Item(String type) {
        this.type = type;
        name = type.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        isRangedWeapon = cfg.getString(type, "effect").equals("shootProjectile");
        defaultAmount = 1;
    }
    
    public String getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public void use(Creature user) {
        new ItemInstance(this).use(user);
    }
    
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
    
    public boolean isRangedWeapon() {
        return isRangedWeapon;
    }
    
    public boolean isPotion() {
        return getName().contains("potion");
    }
    
    public int getDefaultAmount() {
        return defaultAmount;
    }
    
    public int getIntAttribute(String attributeName) {
        return cfg.getInt(name, attributeName);
    }
}
