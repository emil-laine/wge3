package wge3.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.entity.ground.Ground;
import wge3.entity.object.MapObject;
import wge3.interfaces.Drawable;

public class Tile implements Drawable {
    
    public static final int size = 24;
    
    private Area area;
    private int x, y;
    
    private Ground ground;
    private MapObject object;
    
    private boolean needsToBeDrawn;

    public Tile() {
        needsToBeDrawn = true;
    }

    public Ground getGround() {
        return ground;
    }

    public MapObject getObject() {
        return object;
    }
    
    public void setGround(Ground g) {
        ground = g;
    }

    public void setObject(MapObject o) {
        object = o;
    }

    public void removeObject() {
        object = null;
    }

    public boolean isPassable() {
        if (object == null) {
            return true;
        } else {
            return object.isPassable();
        }
    }
    
    @Override
    public void draw(Batch batch) {
        ground.draw(batch);
        if (object != null) object.draw(batch);
        
        needsToBeDrawn = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
        ground.setX(x);
        if (object != null) {
            object.setX(x);
        }
    }

    public void setY(int y) {
        this.y = y;
        ground.setY(y);
        if (object != null) {
            object.setY(y);
        }
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }
}
