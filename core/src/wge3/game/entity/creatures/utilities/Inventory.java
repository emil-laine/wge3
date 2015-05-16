
package wge3.game.entity.creatures.utilities;

import java.util.*;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class Inventory {
    
    private Creature owner;
    private List<InventoryEntry> items;
    private int selectedItem;
    
    public Inventory(Creature owner) {
        this.owner = owner;
        items = new ArrayList();
        selectedItem = 0;
    }
    
    public Item getItem(Item item) {
        for (InventoryEntry inventoryEntry : items) {
            if (inventoryEntry.getItem().equals(item)) {
                return inventoryEntry.getItem();
            }
        }
        return null;
    }
    
    //gets random item, not #0
    public Item getRandomItem() {
        if (items.size() == 1 || items.isEmpty()) {
            return null;
        }
        if (items.size() == 2) {
            return items.get(1).getItem();
        }
        
        Random random = new Random();
        int randNumber = random.nextInt(items.size()-1)+1;
        
        return items.get(randNumber).getItem();
    }
    
    public List<Item> getItems() {
        List<Item> newList = new ArrayList();
        for (InventoryEntry item : items) {
            newList.add(item.getItem());
        }
        return newList;
    }
    
    public void addItem(Item item) {
        addItem(item, item.getDefaultAmount());
    }
    
    public void addItem(Item item, int amount) {
        for (InventoryEntry itemEntry : items) {
            if (itemEntry.getItem().equals(item)) {
                    itemEntry.addAmount(amount);
                    return;
            }
        }
        items.add(new InventoryEntry(item, amount));
    }
    
    public void removeItem(Item item) {
        removeItem(item, 1);
    }
    
    public void removeItem(Item item, int amount) {
        for (InventoryEntry entry : items) {
            if (entry.getItem().equals(item)) {
                if (entry.getAmount() - amount <= 0) {
                    items.remove(entry);
                    owner.setSelectedItem(getNextItem());
                    return;
                }
                entry.removeAmount(amount);
            }
        }
    }
    
    public void removeAllOfAKind(Item item) {
        removeItem(item, getAmount(item));
    }
    
    public int getAmount(Item item) {
        for (InventoryEntry entry : items) {
            if (entry.getItem().equals(item)) {
                return entry.getAmount();
            }
        }
        return 0;
    }
    
    public Item getNextItem() {
        if (items.isEmpty()) {
            selectedItem = 0;
            return null;
        }
        
        if (selectedItem < items.size()) {
            Item item = items.get(selectedItem).getItem();
            selectedItem++;
            return item;
        }
        selectedItem = 0;
        return null;
    }
    
    public void setOwner(Creature owner) {
        this.owner = owner;
    }
}
