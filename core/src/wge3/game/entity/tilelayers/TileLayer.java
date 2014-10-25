package wge3.game.entity.tilelayers;

import wge3.game.entity.tilelayers.mapobjects.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.EnumSet;
import java.util.Set;
import wge3.game.engine.constants.TilePropertyFlag;
import wge3.game.engine.gui.Drawable;
import wge3.game.entity.Tile;

public abstract class TileLayer implements Drawable {
    
    protected Tile tile;
    
    protected final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    protected Sprite sprite;
    
    protected Set<TilePropertyFlag> propertyFlags;
    
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
        propertyFlags = EnumSet.noneOf(TilePropertyFlag.class);
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
        return propertyFlags.contains(TilePropertyFlag.IS_PASSABLE);
    }
    
    public boolean blocksVision() {
        return propertyFlags.contains(TilePropertyFlag.BLOCKS_VISION);
    }

    public boolean drainsHP() {
        return propertyFlags.contains(TilePropertyFlag.DRAINS_HP);
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
    
    public void setSprite(int x, int y) {
        sprite = new Sprite(texture, x*Tile.size, y*Tile.size, Tile.size, Tile.size);
    }
}
