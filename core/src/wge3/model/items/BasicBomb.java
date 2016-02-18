/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import wge3.model.Creature;
import wge3.model.objects.Item;

public final class BasicBomb extends Item {
    
    public BasicBomb() {
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
