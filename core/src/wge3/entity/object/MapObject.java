package wge3.entity.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class MapObject implements Drawable {
    
    protected Area area;
    protected int x, y;
    
    protected Texture sprite;
    
    protected boolean passable;
    protected boolean needsToBeDrawn;

    public MapObject() {
        // default values:
        passable = true;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public MapObject(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, x*Tile.size, y*Tile.size);
    }

    public boolean isPassable() {
        return passable;
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }
}
