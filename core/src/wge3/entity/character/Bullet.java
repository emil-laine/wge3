package wge3.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.interfaces.Drawable;
import wge3.world.Area;

public abstract class Bullet implements Drawable {
         
    protected Area area;
    protected float x;
    protected float y;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected int speed;
    protected boolean exists;

    public Bullet() {
    }
    
    public void setArea(Area area) {
        this.area = area;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }
    
    public boolean exists() {
        return exists;
    }
}
