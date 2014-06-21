package wge3.entity.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import wge3.entity.terrainelements.Item;
import wge3.entity.terrainelements.MapObject;
import static wge3.game.PlayState.mStream;
import wge3.interfaces.Drawable;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Creature implements Drawable {

    protected Area area;
    protected float x;
    protected float y;
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
    protected long timeOfLastRegen;
    protected int strength;
    protected int defense;
    
    protected boolean canSeeEverything;
    protected boolean walksThroughWalls;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected boolean goingForward;
    protected boolean goingBackward;
    protected boolean turningLeft;
    protected boolean turningRight;

    public Creature() {
        size = Tile.size / 3;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        direction = random() * PI2;
        turningSpeed = 3.5f;
        sight = 10;
        FOV = PI/4*3;
        
        HPRegenRate = 1;
        timeOfLastRegen = TimeUtils.millis();
        
        canSeeEverything = false;
        walksThroughWalls = false;
        
        bounds = new Rectangle();
        bounds.height = 0.75f*Tile.size;
        bounds.width = 0.75f*Tile.size;
        
        inventory = new Inventory();
        inventory.setOwner(this);
        
        goingForward = false;
        goingBackward = false;
        turningLeft = false;
        turningRight = false;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
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
        sprite.draw(batch);
    }

    public void turnLeft(float delta) {
        direction += turningSpeed * delta;
        if (direction >= MathUtils.PI2) direction -= MathUtils.PI2;
        updateSpriteRotation();
    }

    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
        if (direction < 0) direction += MathUtils.PI2;
        updateSpriteRotation();
    }

    public void move(float dx, float dy) {
        // Apply movement modifiers:
        float movementModifier = area.getTileAt(getX(), getY()).getMovementModifier();
        dx *= movementModifier;
        dy *= movementModifier;
        
        // Calculate actual movement:
        float destX = getX() + dx;
        float destY = getY() + dy;
        
        if (canMoveTo(destX, destY)) {
            setX(destX);
            setY(destY);
            updateSpritePosition();
        } else if (canMoveTo(getX(), destY)) {
            setY(destY);
            updateSpritePosition();
        } else if (canMoveTo(destX, getY())) {
            setX(destX);
            updateSpritePosition();
        }
        
        // Pick up any items in the new tile:
        // Could this be optimized by using the same tile as in the beginning of this method?
        Tile newTile = area.getTileAt(getX(), getY());
        MapObject object = newTile.getObject();
        if (object != null && object.isItem()) {
            inventory.addItem((Item) object);
            newTile.removeObject();
        }
    }

    public boolean canMoveTo(float x, float y) {
        return area.hasLocation(x, y) && (area.getTileAt(x, y).isPassable() || this.walksThroughWalls());
    }
    
    public void useItem() {
        if (selectedItem == null) attackUnarmed();
        else selectedItem.use(this);
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
        HP -= Math.max(amount - defense, 1);
        if (this.isDead()) {
            area.removeCreature(this);
        }
    }

    public boolean isDead() {
        return HP <= 0;
    }
    
    public void regenerateHP(long currentTime) {
        if (currentTime - timeOfLastRegen > 1000) {
            if (HP < maxHP) {
                HP += HPRegenRate;
                if (HP > maxHP) HP = maxHP;
            }
            timeOfLastRegen = currentTime;
        }
    }
    
    public void updateSpritePosition() {
        sprite.setPosition(getX()-Tile.size/3, getY()-Tile.size/2);
    }
    
    public void updateSpriteRotation() {
        sprite.setRotation(direction*MathUtils.radiansToDegrees);
    }

    public void attackUnarmed() {
        float destX = getX() + MathUtils.cos(direction) * Tile.size/2;
        float destY = getY() + MathUtils.sin(direction) * Tile.size/2;
        Tile destTile = area.getTileAt(destX, destY);
        if (!destTile.getCreatures().contains(this)) {
            if (randomBoolean(0.7f)) {
                mStream.addMessage("*punch*");
                destTile.dealDamage(strength);
            } else {
                mStream.addMessage("*kick*");
                destTile.dealDamage((int) (strength * 1.2f));
            }
            
        }
    }
    
    public boolean isPlayer() {
        return this.getClass() == Player.class;
    }
    
    public void goForward(boolean truth) {
        goingForward = truth;
    }
    
    public void goBackward(boolean truth) {
        goingBackward = truth;
    }
    
    public void turnLeft(boolean truth) {
        turningLeft = truth;
    }
    
    public void turnRight(boolean truth) {
        turningRight = truth;
    }

    public void updatePosition(float delta) {
        if (goingForward) {
            float dx = MathUtils.cos(direction) * currentSpeed * delta;
            float dy = MathUtils.sin(direction) * currentSpeed * delta;
            move(dx, dy);
        } else if (goingBackward) {
            float dx = -(MathUtils.cos(direction) * currentSpeed/1.5f * delta);
            float dy = -(MathUtils.sin(direction) * currentSpeed/1.5f * delta);
            move(dx, dy);
        }
        
        if (turningLeft) {
            turnLeft(delta);
        } else if (turningRight) {
            turnRight(delta);
        }
    }
    
    public boolean canBeSeenBy(Creature creature) {
        return area.getTileAt(getX(), getY()).canBeSeenBy(creature);
    }
    
    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    public Tile getTile() {
        return area.getTileAt(getX(), getY());
    }
}
