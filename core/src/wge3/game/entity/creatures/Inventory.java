
package wge3.game.entity.creatures;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import wge3.game.entity.tilelayers.mapobjects.Item;

public class Inventory {
    
    private Creature owner;
    private Map<Item, Integer> items;
    private Iterator<Item> iterator; // iterator points to current selectedItem of owner.
    
    public Inventory() {
        items = new LinkedHashMap<Item, Integer>();
        iterator = items.keySet().iterator();
    }

    public Set<Item> getItems() {
        return items.keySet();
    }
    
    public void addItem(Item item) {
        addItem(item, item.getDefaultAmount());
    }
    
    public void addItem(Item item, int amount) {
        if (!items.containsKey(item)) {
            items.put(item, amount);
            iterator = items.keySet().iterator();
            owner.setSelectedItem(getNextItem());
        } else {
            items.replace(item, items.get(item) + amount);
        }
    }
    
    public void removeItem(Item item) {
        removeItem(item, 1);
    }
    
    public void removeItem(Item item, int amount) {
        if (items.get(item) - amount <= 0) {
            iterator.remove();
            owner.setSelectedItem(getNextItem());
        } else {
            items.replace(item, items.get(item) - amount);
        }
    }
    
    public int getAmount(Item item) {
        return items.get(item);
    }
    
    public Item getNextItem() {
        if (items.isEmpty()) {
            return null;
        } else if (!iterator.hasNext()) {
            iterator = items.keySet().iterator();
            return null;
        } else {
            return iterator.next();
        }
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }
}
