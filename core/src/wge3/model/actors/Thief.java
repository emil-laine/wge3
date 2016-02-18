/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.actors;

import wge3.model.NonPlayer;
import static com.badlogic.gdx.math.MathUtils.random;
import wge3.model.ai.ThiefAI;

/**
 *
 * @author chang
 */

public class Thief extends NonPlayer{
    
    public Thief() {
    
        setSprite(6, 2);
        HP.setMax(random(60, 90));
        strength = random(5, 12);
        defense = random(3, 7);
        defaultSpeed = random(35, 40);
        currentSpeed = defaultSpeed;
        ai = new ThiefAI(this);
    }
}
