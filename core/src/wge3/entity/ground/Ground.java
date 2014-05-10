package wge3.entity.ground;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.world.Tile;

public abstract class Ground {
    
    protected Tile tile;
    protected int x, y;
    
    protected Color color;
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
    
    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, Tile.size, Tile.size);
        sr.end();
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
