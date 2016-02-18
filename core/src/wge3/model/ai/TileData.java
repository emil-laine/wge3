/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

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
        this(x, y, 0, passable);
    }
    
    public TileData(int x, int y, int counter) {
        this(x, y, counter, true);
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
