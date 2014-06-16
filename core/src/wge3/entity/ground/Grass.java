package wge3.entity.ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.Ground;
import wge3.world.Tile;

public final class Grass extends Ground {

    public Grass() {
        sprite = new Sprite(texture, 0, 0,Tile.size, Tile.size);
        
        movementModifier = 15/16f;
    }

}
