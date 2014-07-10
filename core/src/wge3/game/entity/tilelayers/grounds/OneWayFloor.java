/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
