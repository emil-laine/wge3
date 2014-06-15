package wge3.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;
import wge3.entity.terrainelements.Item;
import wge3.entity.terrainelements.MapObject;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Creature implements Drawable {

    protected Area area;
    protected Rectangle bounds;

    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    protected float FOV;
    
    protected int maxHP;
    protected int HP;
    protected int HPRegenRate;
    
    protected boolean canSeeEverything;
    protected boolean walksThroughWalls;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    protected Texture texture;
    protected Sprite sprite;
    protected boolean needsToBeDrawn;
    protected Random RNG;

    public Creature() {
        RNG = new Random();
        size = Tile.size;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        direction = RNG.nextFloat() * MathUtils.PI2;
        turningSpeed = 3.5f;
        sight = 10;
        FOV = MathUtils.PI/4*3;
        
        maxHP = 100;
        HP = maxHP;
        
        canSeeEverything = false;
        walksThroughWalls = false;
        
        bounds = new Rectangle();
        bounds.height = 0.75f*Tile.size;
        bounds.width = 0.75f*Tile.size;
        
        inventory = new Inventory();
        
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
    
    public void setPosition(int x, int y) {
        bounds.x = x;
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

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int newMaxHP) {
        this.maxHP = newMaxHP;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int newHP) {
        this.HP = newHP;
    }

    public int getSize() {
        return size;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, getX()-Tile.size/3, getY()-Tile.size/2, 7.5f, 12, 15, 24, 1, 1, direction*MathUtils.radiansToDegrees);
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
        // Apply movement modifiers:
        Tile currentTile = area.getTileAt(getX(), getY());
        if (currentTile.affectsMovement()) {
            float movementModifier = currentTile.getMovementModifier();
            dx *= movementModifier;
            dy *= movementModifier;
        }
        
        // Calculate actual movement:
        float destX = getX() + dx;
        float destY = getY() + dy;
        
        if (canMoveTo(destX, destY)) {
            setX(destX);
            setY(destY);
            needsToBeDrawn = true;
        } else if (canMoveTo(getX(), destY)) {
            setY(destY);
            needsToBeDrawn = true;
        } else if (canMoveTo(destX, getY())) {
            setX(destX);
            needsToBeDrawn = true;
        }
        
        // Pick up any items in the tile:
        // Could this be optimized by using the same tile as in the beginning of this method?
        MapObject object = area.getTileAt(getX(), getY()).getObject();
        if (object != null && object.isItem()) {
            inventory.addItem((Item) object);
            currentTile.removeObject();
        }
    }

    public boolean canMoveTo(float x, float y) {
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
        if (selectedItem != null) {
            selectedItem.use();
        }
    }

    public void changeItem() {
        setSelectedItem(inventory.getNextItem());
    }

    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }

    public Item getSelectedItem() {
        return selectedItem;
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

    private boolean isInCenterOfATile() {
        float x = (getX() % Tile.size) / Tile.size;
        float y = (getY() % Tile.size) / Tile.size;
        return (x >= 0.25f && x <= 0.75f) && (y >= 0.25f && y <= 0.75f);
    }

    public void dealDamage(int amount) {
        HP -= amount;
    }

    public boolean isDead() {
        return HP <= 0;
    }
    
    public void regenerateHP(float delta) {
        if (HP < maxHP) {
            HP += HPRegenRate * delta;
            if (HP > maxHP) HP = maxHP;
        }
    }
}
