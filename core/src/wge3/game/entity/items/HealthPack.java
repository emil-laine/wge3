package wge3.game.entity.items;

import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class HealthPack extends Item {

    public HealthPack() {
        setSprite(3, 2);
        name = "health pack";
    }

    @Override
    public void use(Creature user) {
        user.addHP(50);
        user.getInventory().removeItem(this);
    }
}
