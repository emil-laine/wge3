/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import com.badlogic.gdx.utils.Timer;
import static wge3.engine.PlayState.mStream;
import wge3.model.Creature;
import wge3.model.objects.Item;

public final class LevitationPotion extends Item {
    
    private int duration; // seconds

    public LevitationPotion() {
        setSprite(1, 5);
        name = "levitation potion";
        duration = 10;
    }

    @Override
    public void use(final Creature user) {
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setFlying(true);
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                    user.setFlying(false);
            }
        }, duration);
    }
}
