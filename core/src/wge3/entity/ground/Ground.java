package wge3.entity.ground;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.world.Tile;

public abstract class Ground {
    
    protected Tile tile;
    protected int x, y;
    
    protected Texture sprite;
    protected boolean drainsHealth;
    protected boolean slowsMovement;

    public Ground() {
        drainsHealth = false;
        slowsMovement = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void draw(Batch batch) {
        batch.draw(sprite, x, y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean slowsMovement() {
        return slowsMovement;
    }
}
