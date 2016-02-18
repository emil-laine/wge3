/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.entity.tilelayers.mapobjects.walls;

import wge3.game.entity.tilelayers.mapobjects.Wall;

public final class StoneWall extends Wall {
    
    public StoneWall(int type) {
        assert type >= 0 && type <= 2 : "Illegal arguments passed to StoneWall constructor!";
        setSprite(3+type, 1);
    }
    
    @Override
    public void dealDamage(int amount) {
    }
}
