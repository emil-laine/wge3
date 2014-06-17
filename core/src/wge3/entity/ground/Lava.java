package wge3.entity.ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.terrainelements.Ground;
import wge3.world.Tile;

public final class Lava extends Ground {

    public Lava() {
        sprite = new Sprite(texture, 3*Tile.size, 0, Tile.size, Tile.size);
        
        drainsHP = true;
        HPDrainAmount = 2;
        movementModifier = 0.7f;
    }
}
