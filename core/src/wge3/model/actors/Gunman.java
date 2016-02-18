/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.actors;

import wge3.model.NonPlayer;
import wge3.model.ai.RangedAI;
import wge3.model.items.Handgun;

/**
 *
 * @author chang
 */
public final class Gunman extends NonPlayer{
    
    public Gunman() {
        setSprite(4, 3);
        HP.setMax(60);
        strength = 10;
        defense = 5;
        defaultSpeed = 70;
        currentSpeed = 70;
        inventory.addItem(new Handgun(), 60);
        this.changeItem();
        ai = new RangedAI(this);
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
