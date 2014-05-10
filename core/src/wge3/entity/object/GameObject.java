package wge3.entity.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class GameObject {
    
    protected Area area;
    protected int x, y;
    
    protected Color color;
    protected boolean passable;

    public GameObject() {
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
    
    public GameObject(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, Tile.size, Tile.size);
        sr.end();
    }

    public boolean isPassable() {
        return passable;
    }
}
