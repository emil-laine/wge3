package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Tile;

public final class BrickWall extends Wall {

    public BrickWall() {
        sprite = new Sprite(texture, 0, Tile.size, Tile.size, Tile.size);
        hasDestroyedSprite = true;
        maxHP = 150;
        HP = 150;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
        
        if (isDestroyed()) {
            // show destroyed sprite
            sprite.setRegion(2*Tile.size, Tile.size, Tile.size, Tile.size);
            passable = true;
            blocksVision = false;
            movementModifier = 0.7f;
        } else if (HP < maxHP/2) {
            // show damaged sprite
            sprite.setRegion(Tile.size, Tile.size, Tile.size, Tile.size);
            blocksVision = false;
        }
    }
}
