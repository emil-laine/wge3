package wge3.entity.terrainelement;

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
    
    protected boolean needsToBeDrawn;
    
    protected boolean passable;
    protected boolean blocksVision;
    protected boolean drainsHealth;
    protected int healthDrainAmount;
    protected boolean affectsMovement;
    protected float movementModifier;
    // Effects of movementModifier's values:
    // ]1, inf[ -> speeds up movement
    //        1 -> does nothing
    //   ]0, 1[ -> slows down movement
    //        0 -> stops movement
    // Negative values reverse the movement direction, but that's not any useful.
    
    public TerrainElement() {
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
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    public boolean blocksVision() {
        return blocksVision;
    }

    public boolean drainsHealth() {
        return drainsHealth;
    }

    public int getHealthDrainAmount() {
        return healthDrainAmount;
    }

    public boolean affectsMovement() {
        return affectsMovement;
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
