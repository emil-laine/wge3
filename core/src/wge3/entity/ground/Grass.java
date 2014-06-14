package wge3.entity.ground;

import wge3.world.Ground;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Grass extends Ground {

    public Grass() {
        super();
        sprite = new Texture(Gdx.files.internal("graphics/grass.png"));
    }

}
