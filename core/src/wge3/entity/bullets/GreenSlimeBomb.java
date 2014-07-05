package wge3.entity.bullets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import wge3.entity.character.Bullet;
import wge3.entity.mapobjects.GreenSlime;
import wge3.interfaces.Explosive;
import wge3.world.Tile;

public class GreenSlimeBomb extends Bullet implements Explosive {
    
    private Timer timer;
    private Timer.Task task;
    private int time; // in seconds
    private int range;
    private int damage;
    
    public GreenSlimeBomb() {
        sprite = new Sprite(texture, 0, 4*Tile.size, Tile.size, Tile.size);
        
        range = 2;
        time = 2;
        timer = new Timer();
        task = new Timer.Task() {

            @Override
            public void run() {
                explode();
            }
        };
    }

    public int getRange() {
        return range;
    }

    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    
    public void startTimer() {
        timer.scheduleTask(task, time);
    }

    @Override
    public void explode() {
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range) && !currentTile.hasObject()) {
                currentTile.setObject(new GreenSlime());
            }
        }
        area.removeBullet(this);
    }
}
