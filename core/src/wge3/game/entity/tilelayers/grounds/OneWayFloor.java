/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.entity.tilelayers.grounds;

import wge3.game.engine.constants.Direction;
import wge3.game.entity.tilelayers.Ground;

/**
 *
 * @author chang
 */
public final class OneWayFloor extends Ground {
    
    private Direction direction;
    
    public OneWayFloor(Direction direction) {
        this.direction = direction;
        
        int x;
        switch (direction) {
            case UP:    x = 4; break;
            case RIGHT: x = 5; break;
            case DOWN:  x = 6; break;
            default:    x = 7; break;
        }
        setSprite(x, 0);
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
