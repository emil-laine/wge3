package wge3.entity.ground;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.world.Tile;

public abstract class Ground {
    
    public int x, y;
    
    protected Color color;
    protected boolean drainsHealth;
    protected boolean slowsMovement;

    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, Tile.size, Tile.size);
        sr.end();
    }
}
