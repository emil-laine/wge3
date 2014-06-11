package wge3.entity.ground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Water extends Ground {

    public Water() {
        sprite = new Texture(Gdx.files.internal("graphics/water.png"));
        slowsMovement = true;
    }
    
}
