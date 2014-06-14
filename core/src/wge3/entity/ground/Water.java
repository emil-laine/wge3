package wge3.entity.ground;

import wge3.world.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Water extends Ground {

    public Water() {
        texture = new Texture(Gdx.files.internal("graphics/water.png"));
        sprite = new Sprite(texture);
        slowsMovement = true;
    }
}
