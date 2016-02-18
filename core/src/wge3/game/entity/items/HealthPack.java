/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
            Player.statistics.addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, healedAmount);
        }
        else {
            Player.statistics.addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, (user.getMaxHP() - user.getCurrentHP()));
        }
        
        user.getInventory().removeItem(this);
    }
}
