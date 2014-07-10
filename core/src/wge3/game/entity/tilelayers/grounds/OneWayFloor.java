/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.entity.tilelayers.grounds;

import wge3.game.engine.constants.Direction;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

/**
 *
 * @author chang
 */
public final class OneWayFloor extends Ground {
    
    private Direction direction;
    
    public OneWayFloor(Direction direction) {
        this.direction = direction;
        
        int i;
        switch (direction) {
            case UP:    i = 4; break;
            case RIGHT: i = 5; break;
            case DOWN:  i = 6; break;
            default:    i = 7; break;
        }
        sprite = new Sprite(texture, i*Tile.size, 0, Tile.size, Tile.size);
    }
    
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    
    
    
    
}
