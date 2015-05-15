package wge3.game.entity.tilelayers.mapobjects.walls;

import java.util.EnumSet;
import wge3.game.engine.constants.TilePropertyFlag;
import static wge3.game.engine.constants.TilePropertyFlag.BLOCKS_VISION;
import static wge3.game.engine.constants.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.game.engine.constants.TilePropertyFlag.COVERS_WHOLE_TILE;
import static wge3.game.engine.constants.TilePropertyFlag.HAS_DESTROYED_SPRITE;
import wge3.game.entity.tilelayers.mapobjects.Wall;
import wge3.game.entity.Tile;

public final class BrickWall extends Wall {
    
    public BrickWall() {
        setSprite(0, 1);
        propertyFlags = EnumSet.of(HAS_DESTROYED_SPRITE, BLOCKS_VISION, CASTS_SHADOWS, COVERS_WHOLE_TILE);
        maxHP = 150;
        HP = 150;
        hardness = 8;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
        
        if (isDestroyed()) {
            // show destroyed sprite
            sprite.setRegion(2*Tile.size, Tile.size, Tile.size, Tile.size);
            propertyFlags.add(TilePropertyFlag.IS_PASSABLE);
            propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
            propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
            movementModifier = 0.7f;
        } else if (HP < maxHP/2) {
            // show damaged sprite
            sprite.setRegion(Tile.size, Tile.size, Tile.size, Tile.size);
            propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
            propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
        }
    }
}
