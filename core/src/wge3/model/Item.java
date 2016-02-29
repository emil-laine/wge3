/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.math.MathUtils;
import wge3.engine.util.Config;

public class Item extends Entity {
    
    protected String name;
    private boolean canBePickedUp;
    private int value;
    protected int defaultAmount;
    private final Effect useEffect;
    private final boolean isRangedWeapon;
    private static final Config cfg = new Config("config/item.toml");
    
    public Item(String type) {
        super((int) (cfg.getFloat(type, "size") * Tile.size));
        name = type.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        canBePickedUp = true;
        useEffect = new Effect(cfg.getString(type, "effect"), cfg, type);
        isRangedWeapon = cfg.getString(type, "effect").equals("shootProjectile");
        defaultAmount = 1;
        setSprite(cfg.getIntX(type, "spritePos")
                  + MathUtils.random(cfg.getInt(type, "spriteMultiplicity") - 1),
                  cfg.getIntY(type, "spritePos"));
    }
    
    public String getName() {
        return name;
    }
    
    public void use(Creature user) {
//        getComponents().forEach(c -> c.use(user));
        useEffect.activate(user, this);
    }
    
    public boolean canBePickedUp() {
        return canBePickedUp;
    }
    
    void setCanBePickedUp(boolean newState) {
        canBePickedUp = newState;
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
