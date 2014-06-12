package wge3.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import wge3.entity.object.Item;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Character implements Drawable {

    protected Rectangle bounds;

    protected Area area;
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected float turningSpeed;

    protected int maxHealth;
    protected int health;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    protected Texture sprite;
    protected TextureRegion currentSprite;
    protected boolean needsToBeDrawn;

    public Character(Area area) {
        this.area = area;
        size = Tile.size;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        direction = 0f;
        turningSpeed = 3.5f;
        bounds = new Rectangle();
        bounds.height = 0.75f*Tile.size;
        bounds.width = 0.75f*Tile.size;
        bounds.x = 300;
        bounds.y = 300;
        
        inventory = new Inventory();
        // selectedItem = 
        
        needsToBeDrawn = true;
    }
    
    public float getX() {
        return bounds.x;
    }

    public void setX(float x) {
        bounds.x = x;
    }

    public float getY() {
        return bounds.y;
    }

    public void setY(float y) {
        bounds.y = y;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(int defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getTurningSpeed() {
        return turningSpeed;
    }

    public void setTurningSpeed(float turningSpeed) {
        this.turningSpeed = turningSpeed;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(currentSprite, getX()-Tile.size/3, getY()-Tile.size/2, 7.5f, 12, 15, 24, 1, 1, direction*57.2957795f);
        needsToBeDrawn = false;
    }

    public void turnLeft(float delta) {
        direction += turningSpeed * delta;
        drawTilesUnder();
        needsToBeDrawn = true;
    }

    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
        drawTilesUnder();
        needsToBeDrawn = true;
    }

    public void move(float dx, float dy) {
        if (area.getTileAt(getX(), getY()).slowsMovement()) {
            dx /= 2;
            dy /= 2;
        }
        
        float destX = getX() + dx;
        float destY = getY() + dy;
        
        if (canMove(destX, destY)) {
            setX(destX);
            setY(destY);
            drawTilesUnder();
            needsToBeDrawn = true;
        } else if (canMove(getX(), destY)) {
            setY(destY);
            drawTilesUnder();
            needsToBeDrawn = true;
        } else if (canMove(destX, getY())) {
            setX(destX);
            drawTilesUnder();
            needsToBeDrawn = true;
        }
    }

    public boolean canMove(float x, float y) {
        if (x >= area.getSize()*Tile.size || x < 0) {
            return false;
        } else if (y >= area.getSize()*Tile.size || y < 0) {
            return false;
        }
        
        return area.getTileAt(x, y).isPassable();
    }

    public boolean canSee() {
        return true;
    }
    
    public void useItem() {
        selectedItem.use();
    }

    public boolean NeedsToBeDrawn() {
        return needsToBeDrawn;
    }
    
    public void drawTilesUnder() {
        // The following could probably be implemented in a nicer way?
        float x = getX();
        float y = getY();
        area.requestDrawTile(x, y);
        area.requestDrawTile(x - Tile.size, y);
        area.requestDrawTile(x + Tile.size, y);
        area.requestDrawTile(x, y - Tile.size);
        area.requestDrawTile(x, y + Tile.size);
        area.requestDrawTile(x - Tile.size, y - Tile.size);
        area.requestDrawTile(x + Tile.size, y + Tile.size);
        area.requestDrawTile(x - Tile.size, y + Tile.size);
        area.requestDrawTile(x + Tile.size, y - Tile.size);
    }
}
