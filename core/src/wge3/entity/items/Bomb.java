package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.bullets.FusedBomb;
import wge3.entity.character.Creature;
import wge3.entity.terrainelements.Item;
import wge3.world.Tile;

public final class Bomb extends Item {
    
    public Bomb() {
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        name = "bomb";
    }

    @Override
    public void use(Creature user) {
        FusedBomb bomb = new FusedBomb();
        bomb.setPosition(user.getX(), user.getY());
        user.getInventory().removeItem(this);
        user.getArea().addBomb(bomb);
        bomb.startTimer();
    }
}
