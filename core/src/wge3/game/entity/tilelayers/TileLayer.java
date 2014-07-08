package wge3.game.entity.tilelayers;

import wge3.game.entity.tilelayers.mapobjects.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.engine.gui.Drawable;
import wge3.game.entity.Tile;

public abstract class TileLayer implements Drawable {
    
    protected Tile tile;
    
    protected final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    protected Sprite sprite;
    
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
    
    public TileLayer() {
        // Default values:
        drainsHP = false;
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

    public boolean drainsHP() {
        return drainsHP;
    }

    public int getHPDrainAmount() {
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
        return Item.class.isAssignableFrom(this.getClass());
    }
}
