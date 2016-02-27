/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import wge3.model.objects.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.EnumSet;
import java.util.Set;
import wge3.engine.util.Drawable;

public abstract class TileLayer implements Drawable {
    
    private Tile tile;
    
    private final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    private Sprite sprite;
    
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
    
    public Tile getTile() {
        return tile;
    }
    
    public int getX() {
        return tile.getX();
    }
    
    public int getY() {
        return tile.getY();
    }
    
    protected Sprite getSprite() {
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
