package wge3.game.entity.creatures.npcs;

import static com.badlogic.gdx.Gdx.files;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import wge3.game.engine.ai.ThiefAI;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.tilelayers.mapobjects.Item;

/**
 *
 * @author chang
 */

public class Thief extends NonPlayer{
    
    
    public Thief() {
    
        setSprite(6, 2);
        name = "thief";
        HP.setMaximum(random(60, 90));
        strength = random(5, 12);
        defense = random(3, 7);
        defaultSpeed = random(35, 40);
        currentSpeed = defaultSpeed;
        ai = new ThiefAI(this);
        
        
    }
    
}
