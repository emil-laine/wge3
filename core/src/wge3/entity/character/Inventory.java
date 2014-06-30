
package wge3.entity.character;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import wge3.entity.terrainelements.Item;

public class Inventory {
    
    private Creature owner;
    private Map<Item, Integer> items;
    private Iterator<Entry<Item, Integer>> iterator;
    
    public Inventory() {
        items = new LinkedHashMap<Item, Integer>();
        iterator = items.entrySet().iterator();
    }

    public Set<Item> getItems() {
        return items.keySet();
    }
    
    public void addItem(Item item) {
        addItem(item, 1);
    }
    
    public void addItem(Item item, int amount) {
        if (!items.containsKey(item)) {
            items.put(item, amount);
        } else {
            items.replace(item, items.get(item) + amount);
        }
    }
    
    public void removeItem(Item item) {
        removeItem(item, 1);
    }
    
    public void removeItem(Item item, int amount) {
        if (items.get(item) - amount <= 0) {
            items.remove(item);
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
            iterator = items.entrySet().iterator();
            return null;
        } else {
            return iterator.next().getKey();
        }
    }

    public void setOwner(Creature owner) {
        this.owner = owner;
    }
}
