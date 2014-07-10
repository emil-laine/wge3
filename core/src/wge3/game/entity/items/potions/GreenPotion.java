package wge3.game.entity.items.potions;

import wge3.game.entity.bombs.GreenSlimeBomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class GreenPotion extends Item {
    
    public GreenPotion() {
        setSprite(0, 4);
        name = "green potion";
    }
    
    @Override
    public void use(Creature user) {
        GreenSlimeBomb bomb = new GreenSlimeBomb();
        bomb.setPosition(user.getX(), user.getY());
        user.getInventory().removeItem(this);
        user.getArea().addBomb(bomb);
        bomb.startTimer();
    }
}
