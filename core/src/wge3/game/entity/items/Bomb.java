package wge3.game.entity.items;

import wge3.game.entity.bombs.FusedBomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class Bomb extends Item {
    
    public Bomb() {
        setSprite(0, 2);
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
