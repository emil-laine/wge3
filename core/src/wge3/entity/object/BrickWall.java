package wge3.entity.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BrickWall extends Wall {

    public BrickWall() {
        super();
        sprite = new Texture(Gdx.files.internal("graphics/brickwall.png"));
    }
}
