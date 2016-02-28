/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import com.badlogic.gdx.utils.Timer;
import wge3.model.Entity;
import wge3.model.Tile;

public abstract class Bomb extends Entity {
    
    protected boolean exists;
    protected Timer timer;
    protected Timer.Task task;
    protected int time; // in seconds
    protected int range;
    
    public Bomb() {
        super(Tile.size / 2);
        
        exists = true;
        timer = new Timer();
        task = new Timer.Task() {
            @Override
            public void run() {
                explode();
            }
        };
    }
    
    public boolean exists() {
        return exists;
    }
    
    public void startTimer() {
        timer.scheduleTask(task, time);
    }
    
    public void cancelTimer() {
        timer.clear();
    }
    
    public abstract void explode();
    
    public int getRange() {
        return range;
    }
}
