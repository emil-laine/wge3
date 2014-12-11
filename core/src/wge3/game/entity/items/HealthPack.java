package wge3.game.entity.items;

import wge3.game.engine.constants.Statistic;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class HealthPack extends Item {
    private int healedAmount;
    
    public HealthPack() {
        setSprite(3, 2);
        name = "health pack";
        this.healedAmount = 50; 
    }

    @Override
    public void use(Creature user) {
        user.addHP(healedAmount);
        if (user.getCurrentHP() - healedAmount >= 0) {
            user.getStatistics().addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, healedAmount);
        }
        else {
            user.getStatistics().addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, (user.getMaxHP() - user.getCurrentHP()));
        }
        
        user.getInventory().removeItem(this);
    }
}
