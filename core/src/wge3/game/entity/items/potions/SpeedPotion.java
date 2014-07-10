package wge3.game.entity.items.potions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class SpeedPotion extends Item {
    private float speedBoostMultiplier;
    private int duration; // seconds
    
    public SpeedPotion() {
        sprite = new Sprite(texture, 0, 5*Tile.size, Tile.size, Tile.size);
        name = "speed potion";
        speedBoostMultiplier = 2;
        duration = 10;
    }

    @Override
    public void use(final Creature user) {
        mStream.addMessage("*glug*");
        user.removeItem(this);
        user.setCurrentSpeed((int) (user.getCurrentSpeed() * speedBoostMultiplier));
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                user.setCurrentSpeed(user.getDefaultSpeed());
            }
        }, duration);
    }
}
