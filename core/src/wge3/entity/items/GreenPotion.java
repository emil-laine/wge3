package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.bullets.GreenSlimeBomb;
import wge3.entity.character.Creature;
import wge3.entity.terrainelements.Item;
import wge3.world.Tile;

public class GreenPotion extends Item {
    
    public GreenPotion() {
        sprite = new Sprite(texture, 0, 4*Tile.size, Tile.size, Tile.size);
        name = "green potion";
    }
    
    @Override
    public void use(Creature user) {
        GreenSlimeBomb bomb = new GreenSlimeBomb();
        bomb.setPosition(user.getX(), user.getY());
        user.getInventory().removeItem(this);
        user.getArea().addBullet(bomb);
        bomb.startTimer();
    }
}
