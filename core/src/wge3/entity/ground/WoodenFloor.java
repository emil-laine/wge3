package wge3.entity.ground;

import wge3.world.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class WoodenFloor extends Ground {
    
    public WoodenFloor() {
        super();
        texture = new Texture(Gdx.files.internal("graphics/woodenfloor.png"));
        sprite = new Sprite(texture);
    }
}
