package bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import wge3.entity.character.Bullet;
import static wge3.game.PlayState.mStream;
import wge3.interfaces.Explosive;
import wge3.world.Tile;

public class FusedBomb extends Bullet implements Explosive {
    
    private Timer timer;
    private Timer.Task task;
    private int time; // in seconds
    private int range;
    private int damage;
    
    public FusedBomb() {
        texture = new Texture(Gdx.files.internal("graphics/terrain.png"));
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        
        range = 3;
        damage = 50;
        
        time = 3;
        timer = new Timer();
        task = new Timer.Task() {

            @Override
            public void run() {
                explode();
            }
        };
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
        
        float x = this.x;
        float y = this.y;
        int range = this.range * Tile.size;
        for (Tile currentTile : area.getTiles()) {
            float dx = x - currentTile.getX() * Tile.size;
            float dy = y - currentTile.getY() * Tile.size;
            float distance = (float) Math.sqrt(dx*dx + dy*dy);
            if (distance <= range) {
                float intensity = 1f-(distance-1)*(1f/range);
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
    }
}
