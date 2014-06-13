package wge3.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.interfaces.Drawable;
import wge3.world.Tile;

public abstract class MapObject implements Drawable {
    
    protected Tile tile;
    
    protected Texture sprite;
    
    protected boolean passable;
    protected boolean needsToBeDrawn;
    protected boolean blocksVision;

    public MapObject() {
        // Default values:
        passable = true;
        blocksVision = true;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
    
    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, tile.getX()*Tile.size, tile.getY()*Tile.size);
    }

    public boolean isPassable() {
        return passable;
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    public boolean blocksVision() {
        return blocksVision;
    }
}
