package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import wge3.entity.terrainelements.Item;
import static wge3.game.PlayState.mStream;
import wge3.interfaces.Explosive;
import wge3.world.Tile;

public final class Bomb extends Item implements Explosive {

    private Timer timer;
    private Task task;
    private int time; // in seconds
    
    private int damage;
    private int range;
    
    public Bomb() {
        sprite = new Sprite(texture, 0, 2*Tile.size, Tile.size, Tile.size);
        name = "bomb";
        range = 3;
        
        time = 3;
        timer = new Timer();
        task = new Task() {

            @Override
            public void run() {
                explode();
            }
        };
    }
    
    public void startTimer() {
        timer.scheduleTask(task, time);
    }

    @Override
    public void explode() {
        mStream.addMessage("*EXPLOSION*");
        
        int x = this.getX();
        int y = this.getY();
        int range = this.range;
        for (Tile currentTile : tile.getArea().getTiles()) {
            float dx = x - currentTile.getX();
            float dy = y - currentTile.getY();
            float distance = (float) Math.sqrt(dx*dx + dy*dy);
            if (distance <= range) {
                float intensity = 1f-(distance-1)*(1f/range);
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    @Override
    public void use() {
        
    }
}
