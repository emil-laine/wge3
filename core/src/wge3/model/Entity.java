/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import wge3.engine.util.Drawable;
import static wge3.engine.util.Math.floatPosToTilePos;

public abstract class Entity implements Drawable {
    
    private Area area;
    private Rectangle bounds;
    private int previousTileX;
    private int previousTileY;
    private Set<StateFlag> stateFlags;
    private List<Component> components;
    
    private final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    private Sprite sprite;
    
    protected Entity(int size) {
        bounds = new Rectangle(0, 0, size, size);
        stateFlags = EnumSet.noneOf(StateFlag.class);
        components = new ArrayList();
    }
    
    /** Returns the current x position of this Entity. */
    public float getX() {
        return bounds.x + bounds.width/2;
    }
    
    public void setX(float x) {
        setPosition(x, getY());
    }
    
    /** Returns the current y position of this Entity. */
    public float getY() {
        return bounds.y + bounds.height/2;
    }
    
    public void setY(float y) {
        setPosition(getX(), y);
    }
    
    /** Unconditionally moves this Entity to the given position (x, y). */
    public void setPosition(float x, float y) {
        bounds.setCenter(x, y);
        updateSpritePosition();
    }
    
    /** Unconditionally moves this Entity to the middle of the tile located at (x, y). */
    public void setPosition(int x, int y) {
        setPosition(x*Tile.size + Tile.size/2f,
                    y*Tile.size + Tile.size/2f);
    }
    
    /** Returns whether this Entity has just moved onto another tile. */
    public boolean hasMovedToANewTile() {
        return getTileX() != previousTileX
            || getTileY() != previousTileY;
    }
    
    /** Returns the tile this Entity was on before moving to its current tile. */
    public Tile getPreviousTile() {
        return getArea().getTileAt(previousTileX, previousTileY);
    }
    
    protected void setPreviousTilePosition(int x, int y) {
        previousTileX = x;
        previousTileY = y;
    }
    
    /** Returns the x-coordinate of the tile this Entity is currently on. */
    public int getTileX() {
        return floatPosToTilePos(getX());
    }
    
    /** Returns the y-coordinate of the tile this Entity is currently on. */
    public int getTileY() {
        return floatPosToTilePos(getY());
    }
    
    /** Returns the rectangular area that this Entity occupies. */
    public Rectangle getBounds() {
        return bounds;
    }
    
    /** Returns the size of this Entity. */
    public float getSize() {
        assert bounds.width == bounds.height;
        return bounds.width;
    }
    
    /** Returns the current Area this Entity is in. */
    public final Area getArea() {
        return area;
    }
    
    /** Moves the Entity to the given Area. */
    public void setArea(Area area) {
        this.area = area;
    }
    
    /** Called in {@link Area#passTime(float)}. */
    public void update() {}
    
    /** Returns whether the given Creature can see this Entity. */
    public boolean canBeSeenBy(Creature entity) {
        return getTileUnder().canBeSeenBy(entity) && !isInvisible();
    }
    
    /** Returns the tile under the middle point of this Entity. */
    public Tile getTileUnder() {
        return area.getTileAt(getX(), getY());
    }
    
    /** Returns whether this Entity is a player. */
    public boolean isPlayer() {
        return getClass() == Player.class;
    }
    
    /** Activates/deactivates invisibility for this Entity.
     *  @param truth whether the invisibility mode should be activated */
    public void setInvisibility(boolean truth) {
        if (truth) {
            stateFlags.add(StateFlag.IS_INVISIBLE);
            sprite.setAlpha(0.3f);
        } else {
            stateFlags.remove(StateFlag.IS_INVISIBLE);
            sprite.setAlpha(1);
        }
    }
    
    /** Returns whether this Entity has invisibility currently activated. */
    public boolean isInvisible() {
        return stateFlags.contains(StateFlag.IS_INVISIBLE);
    }
    
    protected final Set<StateFlag> getStateFlags() {
        return stateFlags;
    }
    
    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }
    
    protected final void addComponent(Component component) {
        components.add(component);
    }
    
    /** Modulate the graphical representation of this Entity by the given
     *  Color. */
    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    
    /** Specifies the position of this Entity's graphical representation in
     *  the texture file.
     *  @param x the x-coordinate of the sprite
     *  @param y the y-coordinate of the sprite */
    public void setSprite(int x, int y) {
        // TODO: Add support for entities larger than Tile.size.
        sprite = new Sprite(texture, x*Tile.size, y*Tile.size, Tile.size, Tile.size);
    }
    
    /** Moves the graphical representation of this Entity to the Entity's
     *  current position. */
    public void updateSpritePosition() {
        sprite.setPosition(getX() - Tile.size/2, getY() - Tile.size/2);
    }
    
    protected final Sprite getSprite() {
        return sprite;
    }
}
