package wge3.game.entity.creatures;

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
import com.badlogic.gdx.math.Rectangle;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import static java.lang.Math.max;
import java.util.LinkedList;
import java.util.List;
import static wge3.game.engine.constants.Direction.*;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.mapobjects.Item;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.engine.gui.Drawable;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;

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
    protected float walkToRunMultiplier;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    protected float FOV;
    
    protected String name;
    protected int maxHP;
    protected int HP;
    protected int strength;
    protected int defense;
    protected int unarmedAttackSize; // radius
    protected int maxEnergy;
    protected int energy;
    // Regen rates: the amount of milliseconds between regenerating 1 unit.
    protected int HPRegenRate;
    protected int energyRegenRate;
    protected int energyConsumptionRate;
    protected long lastHPRegen;
    protected long lastEnergyRegen;
    protected long lastEnergyConsumption;
    protected boolean isRunning;
    
    protected boolean picksUpItems;
    
    protected boolean canSeeEverything;
    protected boolean isGhost;
    
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
        defaultSpeed = 75;
        currentSpeed = defaultSpeed;
        walkToRunMultiplier = 1.5f;
        direction = random() * PI2;
        turningSpeed = 3.5f;
        sight = 12;
        FOV = PI;
        unarmedAttackSize = Tile.size/2;
        
        HPRegenRate = 1000;
        lastHPRegen = millis();
        lastEnergyRegen = millis();
        lastEnergyConsumption = millis();
        
        canSeeEverything = false;
        isGhost = false;
        
        bounds = new Rectangle();
        bounds.height = 0.75f*Tile.size;
        bounds.width = 0.75f*Tile.size;
        
        inventory = new Inventory(this);
        selectedItem = null;
        
        goingForward = false;
        goingBackward = false;
        turningLeft = false;
        turningRight = false;
        
        maxEnergy = 100;
        energy = 100;
        energyRegenRate = 500;
        energyConsumptionRate = 80;
        isRunning = false;
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

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
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
        if (!this.isGhost) {
            float movementModifier = area.getTileAt(getX(), getY()).getMovementModifier();
            dx *= movementModifier;
            dy *= movementModifier;
        }
        
        if (isRunning()) {
            long currentTime = millis();
            boolean consumeOrTakeDamage = currentTime - lastEnergyConsumption > energyConsumptionRate;
            if (canRun()) {
                dx *= walkToRunMultiplier;
                dy *= walkToRunMultiplier;
                if (consumeOrTakeDamage) {
                    consumeEnergy();
                    lastEnergyConsumption = currentTime;
                }
            } else {
                if (consumeOrTakeDamage) {
                    this.dealDamage(1);
                    lastEnergyConsumption = currentTime;
                }
            }
        }
        
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
        
        // Should be optimized to pick up items only when current tile != previous tile.
        if (picksUpItems()) pickUpItems();
    }

    public void pickUpItems() {
        Tile currentTile = getTile();
        if (currentTile.hasItem()) {
            inventory.addItem((Item) currentTile.getObject());
            currentTile.removeObject();
        }
    }

    public boolean canMoveTo(float x, float y) {
        if (!area.hasLocation(x, y)) {
            return false;
        }
        
        if (this.isGhost()) {
            return true;
        }
        
        if (area.getTileAt(x, y).isOneWay()) {
            OneWayFloor oneWayTile = (OneWayFloor) area.getTileAt(x, y).getGround();
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
    
    public boolean isGhost() {
        return isGhost;
    }
    
    public void toggleGhostMode() {
        isGhost = isGhost == false;
        if (isGhost()) mStream.addMessage("Ghost Mode On");
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
    
    public void regenerate(long currentTime) {
        if (currentTime - lastHPRegen > HPRegenRate) {
            regenerateHP();
            lastHPRegen = currentTime;
        }
        if (!isRunning && currentTime - lastEnergyRegen > energyRegenRate) {
            regenerateEnergy();
            lastEnergyRegen = currentTime;
        }
    }

    public void regenerateEnergy() {
        if (energy < maxEnergy) energy++;
    }

    public void regenerateHP() {
        if (HP < maxHP) HP++;
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
        area.getTileAt(destX, destY).dealDamage(this.strength);
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
    
    public List<Creature> getEnemiesWithinFOV() {
        List<Creature> enemiesWithinFOV = new LinkedList<Creature>();
        for (Creature creature : getArea().getCreatures()) {
            if (creature.isEnemyOf(this) && creature.canBeSeenBy(this)) {
                enemiesWithinFOV.add(creature);
            }
        }
        return enemiesWithinFOV;
    }
    
    public void addHP(int amount) {
        HP += amount;
        if (HP > maxHP) HP = maxHP;
    }
    
    public void addEnergy(int amount) {
        energy += amount;
        if (energy > maxEnergy) energy = maxEnergy;
    }
    
    public float getDistanceTo(float x, float y) {
        float dx = x - this.x;
        float dy = y - this.y;
        return (float) Math.sqrt(dx*dx + dy*dy);
    }
    
    public float getDistanceTo(Creature other) {
        return getDistanceTo(other.getX(), other.getY());
    }
    
    public void startRunning() {
        isRunning = true;
    }
    
    public void stopRunning() {
        isRunning = false;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void consumeEnergy() {
        energy -= 1;
    }
    
    public boolean canRun() {
        return energy > 0;
    }
}
