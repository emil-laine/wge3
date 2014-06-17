package wge3.entity.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import wge3.entity.character.Bullet;
import static wge3.game.PlayState.mStream;
import wge3.interfaces.Explosive;
import wge3.world.Tile;

public final class FusedBomb extends Bullet implements Explosive {
    
    private Timer timer;
    private Timer.Task task;
    private int time; // in seconds
    private int range;
    private int damage;
    
    public FusedBomb() {
        texture = new Texture(Gdx.files.internal("graphics/terrain.png"));
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        
        range = 3;
        damage = 100;
        time = 3;
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
        mStream.addMessage("*EXPLOSION*");
        
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range)) {
                float distance = currentTile.getDistanceTo(x, y) / Tile.size;
                float intensity = 1f - Math.max(distance-1f, 0f) * (1f / range);
                // intensity = 1, when distance = [0,1].
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
        area.removeBullet(this);
    }
}
