package wge3.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Rectangle;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import static java.lang.Math.max;
import java.util.LinkedList;
import java.util.List;
import static wge3.entity.ground.Direction.*;
import wge3.entity.ground.OneWayTile;
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
    
    protected int team;
    // 0 = player team
    // 1 = monster team
    
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    protected float FOV;
    
    protected String name;
    protected int maxHP;
    protected int HP;
    protected int HPRegenRate; /* per second */
    protected long timeOfLastRegen;
    protected int strength;
    protected int defense;
    protected int unarmedAttackSize; /* radius */
    
    protected boolean picksUpItems;
    
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
        texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
        size = Tile.size / 3;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        direction = random() * PI2;
        turningSpeed = 3.5f;
        sight = 12;
        FOV = PI;
        unarmedAttackSize = Tile.size/2;
        
        HPRegenRate = 1;
        timeOfLastRegen = millis();
        
        canSeeEverything = false;
        walksThroughWalls = false;
        
        bounds = new Rectangle();
        bounds.height = 0.75f*Tile.size;
        bounds.width = 0.75f*Tile.size;
        
        inventory = new Inventory();
        inventory.setOwner(this);
        selectedItem = null;
        
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
        if (direction >= PI2) direction -= PI2;
        updateSpriteRotation();
    }

    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
        if (direction < 0) direction += PI2;
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
        if (!picksUpItems()) return;
        Tile newTile = area.getTileAt(getX(), getY());
        MapObject object = newTile.getObject();
        if (object != null && object.isItem()) {
            inventory.addItem((Item) object);
            newTile.removeObject();
        }
    }

    public boolean canMoveTo(float x, float y) {
        if (!area.hasLocation(x, y)) {
            return false;
        }
        
        if (this.walksThroughWalls()) {
            return true;
        }
        
        if (area.getTileAt(x, y).getGround().getClass() == OneWayTile.class) {
            OneWayTile oneWayTile = (OneWayTile) area.getTileAt(x, y).getGround();
            if (oneWayTile.getDirection() == LEFT && x - getX() > 0) {
                return false;
            }
            if (oneWayTile.getDirection() == RIGHT && x - getX() < 0) {
                return false;
            }
            if (oneWayTile.getDirection() == UP && y - getY() < 0) {
                return false;
            }
            if (oneWayTile.getDirection() == DOWN && y - getY() > 0) {
                return false;
            }
        } 
        
        return (area.getTileAt(x, y).isPassable());
        
        
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
        if (walksThroughWalls()) mStream.addMessage("Ghost Mode On");
        else mStream.addMessage("Ghost Mode Off");
    }

    private boolean isInCenterOfATile() {
        float x = (getX() % Tile.size) / Tile.size;
        float y = (getY() % Tile.size) / Tile.size;
        return (x >= 0.25f && x <= 0.75f) && (y >= 0.25f && y <= 0.75f);
    }

    public void dealDamage(int amount) {
        HP -= max(amount - defense, 1);
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
        sprite.setPosition(getX() - Tile.size/2, getY() - Tile.size/2);
    }
    
    public void updateSpriteRotation() {
        sprite.setRotation(direction * radiansToDegrees);
    }

    public void attackUnarmed() {
        float destX = getX() + MathUtils.cos(direction) * Tile.size;
        float destY = getY() + MathUtils.sin(direction) * Tile.size;
        Circle dest = new Circle(destX, destY, getUnarmedAttackSize());
        for (Creature creature : area.getCreatures()) {
            if (dest.contains(creature.getX(), creature.getY())) {
                creature.dealDamage(this.strength);
            }
        }
    }
    
    public boolean isPlayer() {
        return this.getClass() == Player.class;
    }

    public void doMovement(float delta) {
        if (goingForward) {
            goingForward = false;
            float dx = MathUtils.cos(direction) * currentSpeed * delta;
            float dy = MathUtils.sin(direction) * currentSpeed * delta;
            move(dx, dy);
        } else if (goingBackward) {
            goingBackward = false;
            float dx = -(MathUtils.cos(direction) * currentSpeed/1.5f * delta);
            float dy = -(MathUtils.sin(direction) * currentSpeed/1.5f * delta);
            move(dx, dy);
        }
        
        if (turningLeft) {
            turningLeft = false;
            turnLeft(delta);
        } else if (turningRight) {
            turningRight = false;
            turnRight(delta);
        }
    }
    
    public void goForward() {
        goingForward = true;
    }
    
    public void goBackward() {
        goingBackward = true;
    }
    
    public void turnLeft() {
        turningLeft = true;
    }
    
    public void turnRight() {
        turningRight = true;
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

    public int getTeam() {
        return team;
    }
    
    public List<Tile> getPossibleMovementDestinations() {
        List<Tile> tiles = new LinkedList<Tile>();
        for (Tile tile : area.getTiles()) {
            if (tile.canBeSeenBy(this) && tile.isAnOKMoveDestinationFor(this)) tiles.add(tile);
        }
        return tiles;
    }
    
    public Tile getNewMovementDestination() {
        // Returns a random tile from all the tiles that are
        // ok move destinations and can be seen by creature.
        List<Tile> tiles = getPossibleMovementDestinations();
        return tiles.get(random(tiles.size() - 1));
    }

    public String getName() {
        return name;
    }

    public int getUnarmedAttackSize() {
        return unarmedAttackSize;
    }
    
    public boolean isEnemyOf(Creature other) {
        return this.getTeam() != other.getTeam();
    }
    
    public boolean picksUpItems() {
        return picksUpItems;
    }
}
