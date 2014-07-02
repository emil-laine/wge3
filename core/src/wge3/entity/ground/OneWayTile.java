/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.entity.ground;

import wge3.entity.terrainelements.Ground;

/**
 *
 * @author chang
 */
public class OneWayTile extends Ground {
    
    private Direction direction;
    
    public OneWayTile(Direction direction) {
        this.direction = direction;
    }
    
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    
    
    
    
}
