/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.math.MathUtils;

public class ItemInstance extends Entity {
    
    private final Item type;
    private final Effect useEffect;
    private boolean canBePickedUp;
    
    public ItemInstance(Item type) {
        super((int) (Item.cfg.getFloat(type.getType(), "size") * Tile.size));
        this.type = type;
        useEffect = new Effect(Item.cfg.getString(type.getType(), "effect"), Item.cfg, type.getType());
        canBePickedUp = true;
        
        int offsetX = MathUtils.random(Item.cfg.getInt(type.getType(), "spriteMultiplicity") - 1);
        setSprite(Item.cfg.getIntX(type.getType(), "spritePos") + offsetX,
                  Item.cfg.getIntY(type.getType(), "spritePos"));
    }
    
    public ItemInstance(String itemType) {
        this(Item.get(itemType));
    }
    
    public Item getType() {
        return type;
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
}
