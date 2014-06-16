package wge3.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Bullet implements Drawable {
    
    protected Area area;
    protected float x;
    protected float y;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected int speed;
    protected boolean exists;

    public Bullet() {
        exists = true;
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x - Tile.size/2, y - Tile.size/2);
    }
    
    public boolean exists() {
        return exists;
    }
}
