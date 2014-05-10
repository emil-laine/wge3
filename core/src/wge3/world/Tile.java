package wge3.world;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.entity.ground.Ground;
import wge3.entity.object.GameObject;

public class Tile {
    
    public static final int size = 16;
    
    public int x, y;
    
    private Ground ground;
    private GameObject object;

    public Tile() {
    }

    public Ground getGround() {
        return ground;
    }

    public GameObject getObject() {
        return object;
    }
    
    public void setGround(Ground g) {
        ground = g;
    }

    public void setObject(GameObject o) {
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
    
    public void draw(ShapeRenderer sr) {
        ground.draw(sr);
        if (object != null) object.draw(sr);
    }

    public void setX(int x) {
        this.x = x;
        ground.x = x;
        if (object != null) {
            object.setX(x);
        }
    }

    public void setY(int y) {
        this.y = y;
        ground.y = y;
        if (object != null) {
            object.setY(y);
        }
    }
}
