package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.Tile;

public final class Lava extends Ground {

    public Lava() {
        sprite = new Sprite(texture, 3*Tile.size, 0, Tile.size, Tile.size);
        
        drainsHP = true;
        HPDrainAmount = 15;
        movementModifier = 0.5f;
    }
}
