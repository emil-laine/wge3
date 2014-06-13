package wge3.entity.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BrickWall extends Wall {

    public BrickWall() {
        super();
        texture = new Texture(Gdx.files.internal("graphics/brickwall.png"));
        sprite = new Sprite(texture);
    }
}
