/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import wge3.model.Creature;
import wge3.model.objects.Item;

public final class GreenPotion extends Item {
    
    public GreenPotion() {
        setSprite(0, 4);
        name = "green potion";
    }
    
    @Override
    public void use(Creature user) {
        GreenSlimeBomb bomb = new GreenSlimeBomb();
        bomb.setPos(user.getPos());
        user.getInventory().removeItem(this);
        user.getArea().addBomb(bomb);
        bomb.startTimer();
    }
}
