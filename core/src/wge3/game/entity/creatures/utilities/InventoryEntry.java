/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.entity.creatures.utilities;

import wge3.game.entity.tilelayers.mapobjects.Item;

/**
 *
 * @author chang
 */

public final class InventoryEntry {
    private int amount;
    private Item item;
    
    public InventoryEntry() {
        
    }
    
    public InventoryEntry(Item item, int amount) {
        this.amount = amount;
        this.item = item;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public void addAmount(int amount) {
        this.amount += amount;
    }
    
    public void removeAmount(int amount) {
        this.amount -= amount;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
}
