package wge3.game.entity.tilelayers.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;

public class InvisibilityPotion extends Item {

    private int duration; // seconds
    private Timer timer;
    private Task task;
    private Creature user;
    
    public InvisibilityPotion() {
        sprite = new Sprite(texture, 3*Tile.size, 5*Tile.size, Tile.size, Tile.size);
        name = "invisibility potion";
        duration = 10;
        
        task = new Task() {
            @Override
            public void run() {
                endInvisibility();
            }
        };
        timer = new Timer();
    }
    
    @Override
    public void use(Creature user) {
        this.user = user;
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setInvisibility(true);
        timer.scheduleTask(task, duration);
    }
    
    public void stopEffect() {
        timer.clear();
    }
    
    public void endInvisibility() {
        user.setInvisibility(false);
    }
}
