package wge3.entity.ground;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.world.Tile;

public abstract class Ground {
    
    protected Tile tile;
    
    protected Texture sprite;
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
    
    public void draw(Batch batch) {
        batch.draw(sprite, tile.getX()*Tile.size, tile.getY()*Tile.size);
    }
    
    public boolean slowsMovement() {
        return slowsMovement;
    }
}
