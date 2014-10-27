package wge3.game.entity.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import static com.badlogic.gdx.math.Intersector.overlaps;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Vector2;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static wge3.game.engine.constants.Direction.*;
import wge3.game.engine.constants.StateFlag;
import wge3.game.engine.constants.Statistic;
import wge3.game.engine.constants.Team;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.engine.gui.Drawable;
import static wge3.game.engine.utilities.Math.floatPosToTilePos;
import static wge3.game.engine.utilities.Math.getDiff;
import wge3.game.engine.utilities.StatIndicator;
import wge3.game.engine.utilities.Statistics;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.utilities.Inventory;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.entity.tilelayers.mapobjects.Teleport;

public abstract class Creature implements Drawable {

    protected Area area;
    protected Statistics statistics;
    protected int previousTileX;
    protected int previousTileY;
    protected Circle bounds;
    
    protected Team team;
    
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float walkToRunMultiplier;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    protected float FOV;
    
    protected String name;
    protected StatIndicator HP;
    protected StatIndicator energy;
    protected int strength;
    protected int defense;
    protected int unarmedAttackSize; // radius
    // Regen rates: the amount of milliseconds between regenerating 1 unit.
    protected int HPRegenRate;
    protected int energyRegenRate;
    protected int energyConsumptionRate;
    protected long lastHPRegen;
    protected long lastEnergyRegen;
    protected long lastEnergyConsumption;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    protected Texture texture;
    protected Sprite sprite;
    
    protected Set<StateFlag> stateFlags;
    
    public Creature() {
        texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
        size = Tile.size / 3;
        defaultSpeed = 75;
        currentSpeed = defaultSpeed;
        walkToRunMultiplier = 1.35f;
        direction = random() * PI2;
        turningSpeed = 3.5f;
        sight = 12;
        FOV = PI;
        unarmedAttackSize = Tile.size/2;
        
        HP = new StatIndicator();
        energy = new StatIndicator(100);
        
        HPRegenRate = 1000;
        lastHPRegen = millis();
        lastEnergyRegen = millis();
        lastEnergyConsumption = millis();
        energyRegenRate = 500;
        energyConsumptionRate = 80;
        
        bounds = new Circle(new Vector2(), size);
        
        inventory = new Inventory(this);
        selectedItem = null;
        
        stateFlags = EnumSet.noneOf(StateFlag.class);
    }
    
    public float getX() {
        return bounds.x;
    }

    public void setX(float x) {
        bounds.setX(x);
        updateSpritePosition();
    }

    public float getY() {
        return bounds.y;
    }

    public void setY(float y) {
        bounds.setY(y);
        updateSpritePosition();
    }
    
    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
        updateSpritePosition();
    }
    
    public void setPosition(int x, int y) {
        bounds.setPosition(x * Tile.size + Tile.size/2, y * Tile.size + Tile.size/2);
        updateSpritePosition();
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
    
    public void setCurrentSpeed(int speed) {
        this.currentSpeed = speed;
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
        return HP.getMaximum();
    }

    public void setMaxHP(int newMaxHP) {
        HP.setMaximum(newMaxHP);
    }

    public int getEnergy() {
        return energy.getCurrent();
    }

    public int getMaxEnergy() {
        return energy.getMaximum();
    }

    public int getHP() {
        return HP.getCurrent();
    }

    public void setHP(int newHP) {
        HP.setCurrent(newHP);
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
        if (!isGhost()) {
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
                dx *= walkToRunMultiplier * HP.getFraction();
                dy *= walkToRunMultiplier * HP.getFraction();
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
        } else if (canMoveTo(getX(), destY)) {
            setY(destY);
        } else if (canMoveTo(destX, getY())) {
            setX(destX);
        }
        
        // These could be optimized to be checked less than FPS times per second:
        if (hasMovedToANewTile()) {
            if (getTile().hasTeleport() && !getPreviousTile().hasTeleport()) {
                Teleport tele = (Teleport) getTile().getObject();
                tele.teleport(this);
            }
            previousTileX = getTileX();
            previousTileY = getTileY();
            if (picksUpItems()) pickUpItems();
        }
        
        
    }
    public boolean hasMovedToANewTile() {
        return getTileX() != previousTileX
            || getTileY() != previousTileY;
    }
    
    public void pickUpItems() {
        Tile currentTile = getTile();
        if (currentTile.hasItem()) {
            inventory.addItem((Item) currentTile.getObject());
            currentTile.removeObject();
            if (this.isPlayer()) {
                statistics.addStatToPlayer(this, Statistic.ITEMSPICKEDUP, 1);
            }
            
        }
    }

    public boolean canMoveTo(float x, float y) {
        if (!area.hasLocation(x, y)) {
            return false;
        }
        
        if (this.isGhost()) {
            return true;
        }
        
        Tile destination = area.getTileAt(x, y);
        
        if (this.isFlying()) {
            return destination.isPassable();
        }
        
        if (destination.isOneWay()) {
            OneWayFloor oneWayTile = (OneWayFloor) destination.getGround();
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
        
        if (collisionDetected(x, y)) {
            return false;
        }
        
        if (!this.isOnPassableObject()) {
            return true;
        }
        return (destination.isPassable());
    }
    
    public boolean canMoveTo(Tile dest) {
        return findPath(this.getTile(), dest) != null;
    }
    
    public void useItem() {
        if (this.isInvisible()) {
            if (!selectedItem.isPotion()) {
                this.setInvisibility(false);
            }
            
        }
        if (selectedItem == null) attackUnarmed();
        else { 
            selectedItem.use(this);
            if (this.isPlayer()) {
                statistics.addStatToPlayer(this, Statistic.ITEMSUSED, 1);
            }
            
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

    public int getSight() {
        return sight;
    }

    public float getFOV() {
        return FOV;
    }

    public boolean seesEverything() {
        return stateFlags.contains(StateFlag.SEES_EVERYTHING);
    }
    
    public void toggleSeeEverything() {
        if (stateFlags.contains(StateFlag.SEES_EVERYTHING))
            stateFlags.remove(StateFlag.SEES_EVERYTHING);
        else
            stateFlags.add(StateFlag.SEES_EVERYTHING);
    }
    
    public boolean isGhost() {
        return stateFlags.contains(StateFlag.IS_GHOST);
    }
    
    public void toggleGhostMode() {
        if (stateFlags.contains(StateFlag.IS_GHOST)) {
            stateFlags.remove(StateFlag.IS_GHOST);
            mStream.addMessage("Ghost Mode Off");
        }
        else {
            stateFlags.add(StateFlag.IS_GHOST);
            mStream.addMessage("Ghost Mode On");
        }
    }

    public boolean isInCenterOfATile() {
        float x = (getX() % Tile.size) / Tile.size;
        float y = (getY() % Tile.size) / Tile.size;
        return (x >= 0.25f && x < 0.75f) && (y >= 0.25f && y < 0.75f);
    }

    public void dealDamage(int amount) {
        int decreaseAmount = max(amount - defense, 1);
        HP.decrease(decreaseAmount);
        if (this.isPlayer()) {
            statistics.addStatToPlayer(this, Statistic.DAMAGETAKEN, decreaseAmount);
        }
    }

    public boolean isDead() {
        return HP.isEmpty();
    }
    
    public void regenerate(long currentTime) {
        if (currentTime - lastHPRegen > HPRegenRate) {
            regenerateHP();
            lastHPRegen = currentTime;
        }
        if (!isRunning() && currentTime - lastEnergyRegen > energyRegenRate) {
            regenerateEnergy();
            lastEnergyRegen = currentTime;
        }
    }

    public void regenerateEnergy() {
        energy.increase();
    }

    public void regenerateHP() {
        HP.increase();
        if (this.isPlayer()) {
            statistics.addStatToPlayer(this, Statistic.HEALTHREGAINED, 1);
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
                if (creature.getTeam() != getTeam()) {
                    creature.dealDamage(strength);
                    if (this.isPlayer()) {
                        statistics.addStatToPlayer(this, Statistic.DAMAGEDEALT, strength);
                    }
                }
            }
        }
        area.getTileAt(destX, destY).dealDamage(this.strength);
    }
    
    public boolean isPlayer() {
        return this.getClass() == Player.class;
    }

    public void doMovement(float delta) {
        if (isGoingForward()) {
            stopGoingForward();
            float dx = MathUtils.cos(direction) * currentSpeed * delta;
            float dy = MathUtils.sin(direction) * currentSpeed * delta;
            move(dx, dy);
        } else if (isGoingBackward()) {
            stopGoingBackward();
            float dx = -(MathUtils.cos(direction) * currentSpeed/1.5f * delta);
            float dy = -(MathUtils.sin(direction) * currentSpeed/1.5f * delta);
            move(dx, dy);
        }
        
        if (isTurningLeft()) {
            stopTurningLeft();
            turnLeft(delta);
        } else if (isTurningRight()) {
            stopTurningRight();
            turnRight(delta);
        }
    }
    
    public boolean isGoingForward() {
        return stateFlags.contains(StateFlag.GOING_FORWARD);
    }
    
    public void goForward() {
        stateFlags.add(StateFlag.GOING_FORWARD);
    }
    
    public void stopGoingForward() {
        stateFlags.remove(StateFlag.GOING_FORWARD);
    }
    
    public boolean isGoingBackward() {
        return stateFlags.contains(StateFlag.GOING_BACKWARD);
    }
    
    public void goBackward() {
        stateFlags.add(StateFlag.GOING_BACKWARD);
    }
    
    public void stopGoingBackward() {
        stateFlags.remove(StateFlag.GOING_BACKWARD);
    }
    
    public boolean isTurningLeft() {
        return stateFlags.contains(StateFlag.TURNING_LEFT);
    }
    
    public void turnLeft() {
        stateFlags.add(StateFlag.TURNING_LEFT);
    }
    
    public void stopTurningLeft() {
        stateFlags.remove(StateFlag.TURNING_LEFT);
    }
    
    public boolean isTurningRight() {
        return stateFlags.contains(StateFlag.TURNING_RIGHT);
    }
    
    public void turnRight() {
        stateFlags.add(StateFlag.TURNING_RIGHT);
    }
    
    public void stopTurningRight() {
        stateFlags.remove(StateFlag.TURNING_RIGHT);
    }
    
    public boolean canBeSeenBy(Creature creature) {
        return getTile().canBeSeenBy(creature) && !this.isInvisible();
    }
    
    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    public Tile getTile() {
        return area.getTileAt(getX(), getY());
    }
    
    public Tile getPreviousTile() {
        return area.getTileAt(previousTileX, previousTileY);
    }

    public Team getTeam() {
        return team;
    }
    
    public List<Tile> getPossibleMovementDestinations() {
        List<Tile> tiles = new ArrayList<Tile>();
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
        return stateFlags.contains(StateFlag.PICKS_UP_ITEMS);
    }
    
    //SORT THIS, make comparator
    public List<Creature> getEnemiesWithinFOV() {
        List<Creature> enemiesWithinFOV = new ArrayList<Creature>();
        for (Creature creature : getArea().getCreatures()) {
            if (creature.isEnemyOf(this) && creature.canBeSeenBy(this)) {
                enemiesWithinFOV.add(creature);
            }
        }
        return enemiesWithinFOV;
    }
    
    public List<Creature> getFriendliesWithinFOV() {
        List<Creature> friendlies = new ArrayList<Creature>();
        for (Creature creature : getArea().getCreatures()) {
            if (!creature.isEnemyOf(this) && creature.canBeSeenBy(this)) {
                friendlies.add(creature);
            }
        }
        return friendlies;
        
    }
    
    public void addHP(int amount) {
        HP.increase(amount);
        if (this.isPlayer()) {
            statistics.addStatToPlayer(this, Statistic.HEALTHREGAINED, amount);
        }
        
    }
    
    public void addEnergy(int amount) {
        energy.increase(amount);
    }
    
    public float getDistanceTo(float x, float y) {
        float dx = x - getX();
        float dy = y - getY();
        return (float) Math.sqrt(dx*dx + dy*dy);
    }
    
    public float getDistance2To(float x, float y) {
        float dx = x - getX();
        float dy = y - getY();
        return (dx * dx) + (dy * dy);
    }
    
    public float getDistanceInTilesTo(float x, float y) {
        return getDistanceTo(x, y) / Tile.size;
    }
    
    public float getDistance2InTilesTo(float x, float y) {
        return getDistance2To(x, y) / Tile.size;
    }
    
    public float getDistanceTo(Creature other) {
        return getDistanceTo(other.getX(), other.getY());
    }
    
    public float getDistance2To(Creature other) {
        return getDistance2To(other.getX(), other.getY());
    }
    
    public float getDistanceTo(Tile tile) {
        // Returns distance to middle point of tile
        return getDistanceInTilesTo(tile.getMiddleX(), tile.getMiddleY());
    }
    
    public float getDistance2To(Tile tile) {
        // Returns squared distance to middle point of tile
        return getDistance2InTilesTo(tile.getMiddleX(), tile.getMiddleY());
    }
    
    public void startRunning() {
        stateFlags.add(StateFlag.IS_RUNNING);
    }
    
    public void stopRunning() {
        stateFlags.remove(StateFlag.IS_RUNNING);
    }
    
    public boolean isRunning() {
        return stateFlags.contains(StateFlag.IS_RUNNING);
    }
    
    public void consumeEnergy() {
        energy.decrease();
    }
    
    public boolean canRun() {
        return !energy.isEmpty();
    }
    
    public int getTileX() {
        return floatPosToTilePos(getX());
    }
    
    public int getTileY() {
        return floatPosToTilePos(getY());
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public boolean isInSameTileAs(Creature other) {
        return this.getTileX() == other.getTileX()
            && this.getTileY() == other.getTileY();
    }
    
    public void setInvisibility(boolean truth) {
        if (truth) sprite.setAlpha(0.3f);
        else sprite.setAlpha(1);
        if (truth)
            stateFlags.add(StateFlag.IS_INVISIBLE);
        else
            stateFlags.remove(StateFlag.IS_INVISIBLE);
    }
    
    public boolean isInvisible() {
        return stateFlags.contains(StateFlag.IS_INVISIBLE);
    }
    
    public void removeItem(Item item) {
        inventory.removeItem(item);
    }
    
    public boolean isFacing(Creature target) {
        return abs(getDiff(this.getDirection(), target.direction)) < PI/48;
    }
    
    public boolean isFlying() {
        return stateFlags.contains(StateFlag.IS_FLYING);
    }
    
    public void setFlying(boolean truth) {
        if (truth)
            stateFlags.add(StateFlag.IS_FLYING);
        else
            stateFlags.remove(StateFlag.IS_FLYING);
    }
    
    public void setSprite(int x, int y) {
        sprite = new Sprite(texture, x*Tile.size, y*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
    }
    
    public float getHPAsFraction() {
        return HP.getFraction();
    }
    
    public float getEnergyAsFraction() {
        return energy.getFraction();
    }
    
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
    
    public Statistics getStatistics() {
        return statistics;
    }
    
    public int getDefence() {
        return this.defense;
    }
    
    public boolean isOnPassableObject() {
        return (this.getTile().isPassable());
    }
    
    public Circle getBounds() {
        return bounds;
    }

    private boolean collisionDetected(float x, float y) {
        Circle newBounds = new Circle(x, y, getSize());
        List<Creature> creatures = area.getCreaturesNear(x, y);
        creatures.remove(this);
        for (Creature other : creatures) {
            if (overlaps(newBounds, other.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public List<Creature> getNearbyCreatures() {
        List<Creature> creatures = new ArrayList<Creature>();
        for (Tile tile : getNearbyTiles(true)) {
            creatures.addAll(tile.getCreatures());
        }
        creatures.addAll(getTile().getCreatures());
        creatures.remove(this);
        return creatures;
    }
    
    public List<Tile> getNearbyTiles(boolean checkForDiagonal) {
        return getTile().getNearbyTiles(checkForDiagonal);
    }
}
