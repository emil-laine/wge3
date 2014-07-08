package wge3.game.entity.tilelayers.mapobjects.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.entity.Tile;

public class HealthPack extends Item {

    public HealthPack() {
        sprite = new Sprite(texture, 3*Tile.size, 2*Tile.size, Tile.size, Tile.size);
        name = "health pack";
    }

    @Override
    public void use(Creature user) {
        user.addHP(50);
        user.getInventory().removeItem(this);
    }
}
