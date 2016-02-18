/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
