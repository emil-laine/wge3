
package wge3.entity.character;

import java.util.*;
import wge3.entity.terrainelement.Item;

public class Inventory {
    private Map<Item, Integer> items;
    
    public Inventory() {
        items = new HashMap<Item, Integer>();
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
    
    public Item getItem(int index) {
        return null;
    }
}
