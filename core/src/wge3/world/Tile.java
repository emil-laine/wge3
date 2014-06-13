package wge3.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import wge3.entity.character.Creature;
import wge3.entity.ground.Ground;
import wge3.entity.object.MapObject;
import wge3.interfaces.Drawable;

public class Tile implements Drawable {
    
    public static final int size = 24;
    
    private Area area;
    private int x, y;
    private Rectangle bounds;
    
    private Ground ground;
    private MapObject object;
    
    private boolean needsToBeDrawn;

    public Tile() {
        bounds = new Rectangle();
        bounds.width = Tile.size;
        bounds.height = Tile.size;
        needsToBeDrawn = true;
    }

    public Ground getGround() {
        return ground;
    }

    public MapObject getObject() {
        return object;
    }
    
    public void setGround(Ground g) {
        g.setTile(this);
        ground = g;
    }

    public void setObject(MapObject o) {
        o.setTile(this);
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
        if (object != null) object.draw(batch);
        else ground.draw(batch);
        
        needsToBeDrawn = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Area getArea() {
        return area;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setX(int x) {
        this.x = x;
        bounds.x = x * Tile.size;
    }

    public void setY(int y) {
        this.y = y;
        bounds.y = y * Tile.size;
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    public boolean slowsMovement() {
        return ground.slowsMovement();
    }
    
    public boolean blocksVision() {
        if (object != null) {
            return object.blocksVision();
        } else {
            return false;
        }
    }
    
    public boolean canBeSeenBy(Creature c) {
        float dx = c.getX() - bounds.x;
        float dy = c.getY() - bounds.y;
        // If dx < 0, the tile is to the right of c.
        // If dx > 0, the tile is to the left of c.
        // If dy < 0, the tile is above c.
        // If dy > 0, the tile is below c.
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
        return distance <= c.getSight() * Tile.size;
    }
}
