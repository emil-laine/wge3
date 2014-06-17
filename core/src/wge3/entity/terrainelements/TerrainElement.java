package wge3.entity.terrainelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.interfaces.Drawable;
import wge3.world.Tile;

public abstract class TerrainElement implements Drawable {
    
    protected Tile tile;
    
    protected final static Texture texture = new Texture(Gdx.files.internal("graphics/terrain.png"));
    protected Sprite sprite;
    protected int spriteX;
    protected int spriteY;
    
    protected boolean passable;
    protected boolean blocksVision;
    protected boolean drainsHP;
    protected int HPDrainAmount;
    protected float movementModifier;
    // Effects of movementModifier's values:
    // ]1, inf[ -> speeds up movement
    //        1 -> does nothing
    //   ]0, 1[ -> slows down movement
    //        0 -> stops movement
    // Negative values reverse the movement direction, but that's not any useful.
    
    public TerrainElement() {
        // Default values:
        movementModifier = 1f;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
    
    public int getX() {
        return tile.getX();
    }
    
    public int getY() {
        return tile.getY();
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }
    
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    public boolean isPassable() {
        return passable;
    }
    
    public boolean blocksVision() {
        return blocksVision;
    }

    public boolean drainsHealth() {
        return drainsHP;
    }

    public int getHealthDrainAmount() {
        return HPDrainAmount;
    }
    
    public float getMovementModifier() {
        return movementModifier;
    }

    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    public void setPosition(int x, int y) {
        sprite.setPosition(x, y);
    }

    public boolean isItem() {
        return this.getClass().getSuperclass() == Item.class;
    }
}
