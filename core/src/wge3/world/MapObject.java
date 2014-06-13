package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.interfaces.Drawable;

public abstract class MapObject implements Drawable {
    
    protected Tile tile;
    
    protected Texture texture;
    protected Sprite sprite;
    
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
    
    public int getX() {
        return tile.getX();
    }
    
    public int getY() {
        return tile.getY();
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }
    
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
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

    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    public void setPosition(int x, int y) {
        sprite.setPosition(x, y);
    }
}
