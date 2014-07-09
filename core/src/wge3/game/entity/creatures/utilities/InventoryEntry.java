/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.entity.creatures.utilities;

import wge3.game.entity.tilelayers.mapobjects.Item;

/**
 *
 * @author chang
 */


public class InventoryEntry {
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
