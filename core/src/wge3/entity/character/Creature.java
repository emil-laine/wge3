package wge3.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import wge3.entity.object.Item;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Creature implements Drawable {

    protected Rectangle bounds;

    protected Area area;
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    protected float FOV;
    
    protected int maxHealth;
    protected int health;
    protected boolean canSeeEverything;
    protected boolean walksThroughWalls;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    protected Texture sprite;
    protected TextureRegion currentSprite;
    protected boolean needsToBeDrawn;

    public Creature() {
        size = Tile.size;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        direction = 0;
        turningSpeed = 3.5f;
        sight = 10;
        FOV = MathUtils.PI/4*3;
        canSeeEverything = false;
        walksThroughWalls = false;
        
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
        batch.draw(currentSprite, getX()-Tile.size/3, getY()-Tile.size/2, 7.5f, 12, 15, 24, 1, 1, direction*MathUtils.radiansToDegrees);
        needsToBeDrawn = false;
    }

    public void turnLeft(float delta) {
        direction += turningSpeed * delta;
        if (direction >= MathUtils.PI2) direction -= MathUtils.PI2;
        needsToBeDrawn = true;
    }

    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
        if (direction < 0) direction += MathUtils.PI2;
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
            needsToBeDrawn = true;
        } else if (canMove(getX(), destY)) {
            setY(destY);
            needsToBeDrawn = true;
        } else if (canMove(destX, getY())) {
            setX(destX);
            needsToBeDrawn = true;
        }
    }

    public boolean canMove(float x, float y) {
        if (x >= area.getSize()*Tile.size || x < 0) {
            return false;
        } else if (y >= area.getSize()*Tile.size || y < 0) {
            return false;
        }
        
        if (this.walksThroughWalls()) {
            return true;
        } else {
            return area.getTileAt(x, y).isPassable();
        }
    }
    
    public void useItem() {
        selectedItem.use();
    }

    public boolean NeedsToBeDrawn() {
        return needsToBeDrawn;
    }

    public int getSight() {
        return sight;
    }

    public float getFOV() {
        return FOV;
    }

    public boolean canSeeEverything() {
        return canSeeEverything;
    }
    
    public void toggleCanSeeEverything() {
        canSeeEverything = canSeeEverything == false;
    }
    
    public boolean walksThroughWalls() {
        return walksThroughWalls;
    }
    
    public void toggleWalksThroughWalls() {
        walksThroughWalls = walksThroughWalls == false;
    }
}
