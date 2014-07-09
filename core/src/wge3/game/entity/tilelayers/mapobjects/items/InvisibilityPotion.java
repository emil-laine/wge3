package wge3.game.entity.tilelayers.mapobjects.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public class InvisibilityPotion extends Item {

    private int duration; // seconds
    private Creature user;
    
    public InvisibilityPotion() {
        sprite = new Sprite(texture, 3*Tile.size, 5*Tile.size, Tile.size, Tile.size);
        name = "invisibility potion";
        duration = 10;
        
    }
    
    @Override
    public void use(Creature user) {
        this.user = user;
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setInvisibility(true);
        new Timer().scheduleTask(new Task() {

            @Override
            public void run() {
                endInvisibility();
            }
        }, duration);
    }
    
    public void endInvisibility() {
        user.setInvisibility(false);
    }
}
