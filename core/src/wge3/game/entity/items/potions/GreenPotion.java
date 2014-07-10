package wge3.game.entity.items.potions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.bombs.GreenSlimeBomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.entity.Tile;

public final class GreenPotion extends Item {
    
    public GreenPotion() {
        sprite = new Sprite(texture, 0, 4*Tile.size, Tile.size, Tile.size);
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
