package wge3.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

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
}
