/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import wge3.model.Creature;
import wge3.engine.util.Drawable;
import wge3.model.Area;
import wge3.model.Tile;

public abstract class Bomb implements Drawable {
    
    protected Area area;
    protected float x;
    protected float y;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected boolean exists;
    protected Timer timer;
    protected Timer.Task task;
    protected int time; // in seconds
    protected int range;
    
    public Bomb() {
        texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
        exists = true;
        
        timer = new Timer();
        task = new Timer.Task() {
            @Override
            public void run() {
                explode();
            }
        };
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x - Tile.size/2, y - Tile.size/2);
    }
    
    public boolean exists() {
        return exists;
    }
    
    public boolean canBeSeenBy(Creature creature) {
        return area.getTileAt(getX(), getY()).canBeSeenBy(creature);
    }
    
    public void setLighting(Color color) {
        sprite.setColor(color);
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
    
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    
    public void setSprite(int x, int y) {
        sprite = new Sprite(texture, x*Tile.size, y*Tile.size, Tile.size, Tile.size);
    }
}
