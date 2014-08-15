
package wge3.game.engine.utilities.pathfinding;

import java.util.LinkedList;
import java.util.List;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;

/**
 *
 * @author chang
 */

public class TileData {
    private int x;
    private int y;
    private int counter;
    private boolean passable;

    public TileData(int x, int y, boolean passable) {
        this.x = x;
        this.y = y;
        this.passable = passable;
        this.counter = 0;
    }

    public TileData(int x, int y, int counter, boolean passable) {
        this.x = x;
        this.y = y;
        this.counter = counter;
        this.passable = passable;
    }
    
    

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }
    
    
    public boolean isNextTo(TileData tileData) {
       int x = tileData.getX();
       int y = tileData.getY();
       
        if (x == this.x && y == this.y+1) {
            return true;
        }
        if (x == this.x && y == this.y-1) {
            return true;
        }
        if (y == this.y && x == this.x+1) {
            return true;
        }
        if (y == this.y && x == this.x-1) {
            return true;
        }
        
       return false;
    }
}
