package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Ground {
    
    protected Tile tile;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected boolean drainsHealth;
    protected boolean slowsMovement;

    public Ground() {
        // Default values:
        drainsHealth = false;
        slowsMovement = false;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }
    
    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    
    public boolean slowsMovement() {
        return slowsMovement;
    }

    public void setLighting(Color color) {
        sprite.setColor(color);
    }

    public void setPosition(int x, int y) {
        sprite.setPosition(x, y);
    }
}
