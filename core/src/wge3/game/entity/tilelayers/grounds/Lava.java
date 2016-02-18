/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.entity.tilelayers.grounds;

import java.util.EnumSet;
import static wge3.game.engine.constants.TilePropertyFlag.DRAINS_HP;
import static wge3.game.engine.constants.TilePropertyFlag.IS_PASSABLE;
import wge3.game.entity.tilelayers.Ground;

public final class Lava extends Ground {
    
    public Lava() {
        setSprite(3, 0);
        propertyFlags = EnumSet.of(DRAINS_HP, IS_PASSABLE);
        HPDrainAmount = 15;
        movementModifier = 0.5f;
    }
}
