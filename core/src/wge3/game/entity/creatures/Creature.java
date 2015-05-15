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
import java.util.stream.Collectors;
import static wge3.game.engine.constants.Direction.*;
import wge3.game.engine.constants.StateFlag;
import wge3.game.engine.constants.Statistic;
import wge3.game.engine.constants.Team;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.engine.gui.Drawable;
import static wge3.game.engine.utilities.Math.floatPosToTilePos;
import static wge3.game.engine.utilities.Math.getDiff;
import static wge3.game.engine.utilities.Math.getDistance;
import wge3.game.engine.utilities.StatIndicator;
import static wge3.game.engine.utilities.pathfinding.PathFinder.findPath;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.utilities.Inventory;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.entity.tilelayers.mapobjects.Teleport;

public abstract class Creature implements Drawable {
    
    protected Area area;
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
    
    protected final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    protected Sprite sprite;
    
    protected Set<StateFlag> stateFlags;
    
    public Creature() {
        size = Tile.size / 3;
        defaultSpeed = 75;
        currentSpeed = defaultSpeed;
        walkToRunMultiplier = 1.35f;
        direction = random() * PI2;
        turningSpeed = 4;
        sight = 12;
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
    
    /** Returns the current x position of this Creature. */
    public float getX() {
        return bounds.x;
    }
    
    public void setX(float x) {
        bounds.setX(x);
        updateSpritePosition();
    }
    
    /** Returns the current y position of this Creature. */
    public float getY() {
        return bounds.y;
    }

    public void setY(float y) {
        bounds.setY(y);
        updateSpritePosition();
    }
    
    /** Moves the Creature to the given position (x, y). */
    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
        updateSpritePosition();
    }
    
    /** Moves the Creature to the middle of the tile located at (x, y). */
    public void setPosition(int x, int y) {
        bounds.setPosition(x * Tile.size + Tile.size/2, y * Tile.size + Tile.size/2);
        updateSpritePosition();
    }
    
    /** Returns the current Area this Creature is in. */
    public Area getArea() {
        return area;
    }
    
    /** Moves the Creature to the given Area. */
    public void setArea(Area area) {
        this.area = area;
    }
    
    /** Returns the default walking speed of this Creature. */
    public int getDefaultSpeed() {
        return defaultSpeed;
    }
    
    /** Changes the default walking of this Creature. */
    public void setDefaultSpeed(int defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }
    
    /** Changes the current moving speed of this Creature. */
    public void setCurrentSpeed(int speed) {
        this.currentSpeed = speed;
    }
    
    /** Returns the direction this Creature is currently facing. */
    public float getDirection() {
        return direction;
    }
    
    /** Returns the current turning speed of this Creature. */
    public float getTurningSpeed() {
        return turningSpeed;
    }
    
    /** Changes the current turning speed of this Creature. */
    public void setTurningSpeed(float turningSpeed) {
        this.turningSpeed = turningSpeed;
    }
    
    /** Returns the StatIndicator representing this Creature's HP. */
    public StatIndicator getHP() {
        return HP;
    }
    
    /** Returns the StatIndicator representing this Creature's energy. */
    public StatIndicator getEnergy() {
        return energy;
    }
    
    /** Returns the maximum HP of this Creature. */
    public int getMaxHP() {
        return HP.getMax();
    }
    
    /** Returns the current energy of this Creature. */
    public int getCurrentEnergy() {
        return energy.getCurrent();
    }
    
    /** Returns the maximum energy of this Creature. */
    public int getMaxEnergy() {
        return energy.getMax();
    }
    
    /** Returns the current HP of this Creature. */
    public int getCurrentHP() {
        return HP.getCurrent();
    }
    
    /** Returns the size of this Creature. */
    public int getSize() {
        return size;
    }
    
    /** Returns the current speed of this Creature. */
    public int getCurrentSpeed() {
        return currentSpeed;
    }
    
    /** Returns this Creature's inventory. */
    public Inventory getInventory() {
        return inventory;
    }
    
    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    
    /** Turns the Creature left by the amount specified by turningSpeed.
     *  @param delta seconds since last frame */
    public void turnLeft(float delta) {
        direction += turningSpeed * delta;
        if (direction >= PI2) direction -= PI2;
        updateSpriteRotation();
    }
    
    /** Turns the Creature right by the amount specified by turningSpeed.
     *  @param delta seconds since last frame */
    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
        if (direction < 0) direction += PI2;
        updateSpriteRotation();
    }
    
    /** Tries to move the Creature by dx and dy.
     *  @param dx movement along x-axis
     *  @param dy movement along y-axis */
    public void move(float dx, float dy) {
        // Apply movement modifiers:
        if (!isGhost()) {
            float movementModifier = area.getTileAt(getX(), getY()).getMovementModifier();
            dx *= movementModifier;
            dy *= movementModifier;
        }
        
        if (isRunning() && canRun()) {
            long currentTime = millis();
            dx *= walkToRunMultiplier;
            dy *= walkToRunMultiplier;
            if (currentTime - lastEnergyConsumption > energyConsumptionRate) {
                consumeEnergy();
                lastEnergyConsumption = currentTime;
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
            if (getTileUnder().hasTeleport() && !getPreviousTile().hasTeleport()) {
                Teleport tele = (Teleport) getTileUnder().getObject();
                tele.teleport(this);
            }
            previousTileX = getTileX();
            previousTileY = getTileY();
            if (picksUpItems()) pickUpItems();
        }
    }
    
    /** Returns whether this Creature has just moved onto another tile. */
    public boolean hasMovedToANewTile() {
        return getTileX() != previousTileX
            || getTileY() != previousTileY;
    }
    
    /** Removes any items on the tile the Creature is standing on, and adds them
     *  to the Creature's inventory. */
    public void pickUpItems() {
        Tile currentTile = getTileUnder();
        if (currentTile.hasItem()) {
            inventory.addItem((Item) currentTile.getObject());
            currentTile.removeObject();
            if (this.isPlayer()) {
                Player.getStats().addStatToPlayer((Player) this, Statistic.ITEMSPICKEDUP, 1);
            }
        }
    }
    
    /** Returns whether this Creature can move to the specified point (x, y). */
    public boolean canMoveTo(float x, float y) {
        if (!area.hasLocation(x, y))
            return false;
        
        if (this.isGhost())
            return true;
        
        Tile destination = area.getTileAt(x, y);
        
        if (this.isFlying())
            return destination.isPassable();
        
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
        
        if (collisionDetected(x, y))
            return false;
        
        if (!this.isOnPassableObject())
            return true;
        
        return (destination.isPassable());
    }
    
    /** Returns whether this Creature can move onto the given Tile. */
    public boolean canMoveTo(Tile dest) {
        return findPath(this.getTileUnder(), dest) != null;
    }
    
    /** Calls the use() method on the currently equipped item, or performs an
     *  unarmed attack if no item is currently equipped. */
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
                Player.getStats().addStatToPlayer((Player) this, Statistic.ITEMSUSED, 1);
            }
            
        }
    }
    
    /** Equips the next item from inventory. */
    public void changeItem() {
        setSelectedItem(inventory.getNextItem());
    }
    
    /** Equips the given item. */
    public void setSelectedItem(Item selectedItem) {
        this.selectedItem = selectedItem;
    }
    
    /** Returns the currently equipped item. */
    public Item getSelectedItem() {
        return selectedItem;
    }
    
    /** Returns the sight attribute of this Creature. */
    public int getSight() {
        return sight;
    }
    
    /** Returns whether this Creature can see everything. */
    public boolean seesEverything() {
        return stateFlags.contains(StateFlag.SEES_EVERYTHING);
    }
    
    /** Changes the state of the SEES_EVERYTHING flag. */
    public void toggleSeeEverything() {
		if (stateFlags.contains(StateFlag.SEES_EVERYTHING)) {
            stateFlags.remove(StateFlag.SEES_EVERYTHING);
		} else {
            stateFlags.add(StateFlag.SEES_EVERYTHING);
			getArea().getTiles().stream().forEach((tile) -> tile.setLighting(Color.WHITE));
		}
    }
    
    /** Returns whether this Creature is a ghost. */
    public boolean isGhost() {
        return stateFlags.contains(StateFlag.IS_GHOST);
    }
    
    /** Changes the state of the IS_GHOST flag. */
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
    
    /** Deals damage to this Creature.
     *  @param amount how much damage */
    public void dealDamage(int amount) {
        int decreaseAmount = max(amount - defense, 1);
        HP.decrease(decreaseAmount);
        if (this.isPlayer()) {
            Player.getStats().addStatToPlayer((Player) this, Statistic.DAMAGETAKEN, decreaseAmount);
        }
    }
    
    /** Returns whether this Creature is dead. */
    public boolean isDead() {
        return HP.isEmpty();
    }
    
    /** Regenerates the HP and energy of this Creature, if needed.
     *  @param currentTime the current time, used to decide whether regeneration
     *                     is needed */
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
    
    /** Regenerates the energy of this Creature. */
    private void regenerateEnergy() {
        energy.increase();
    }
    
    /** Regenerates the HP of this Creature. */
    private void regenerateHP() {
        HP.increase();
        if (this.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) this, Statistic.HEALTHREGAINED, 1);
        } 
    }
    
    /** Moves the graphical representation of this Creature to the Creature's
     *  current position. */
    public void updateSpritePosition() {
        sprite.setPosition(getX() - Tile.size/2, getY() - Tile.size/2);
    }
    
    /** Rotates the graphical representation of this Creature according to
     *  the direction the Creature is currently facing. */
    public void updateSpriteRotation() {
        sprite.setRotation(direction * radiansToDegrees);
    }
    
    /** Performs an unarmed attack targeted at a circular area directly in front
     *  of the Creature. */
    public void attackUnarmed() {
        float destX = getX() + MathUtils.cos(direction) * Tile.size;
        float destY = getY() + MathUtils.sin(direction) * Tile.size;
        Circle dest = new Circle(destX, destY, getUnarmedAttackSize());
        for (Creature creature : area.getCreatures()) {
            if (dest.contains(creature.getX(), creature.getY())) {
                if (creature.getTeam() != getTeam()) {
                    creature.dealDamage(strength);
                    if (this.isPlayer()) {
                        Player.statistics.addStatToPlayer((Player) this, Statistic.DAMAGEDEALT, strength);
                    }
                }
            }
        }
        area.getTileAt(destX, destY).dealDamage(this.strength);
    }
    
    /** Returns whether this Creature is a player. */
    public boolean isPlayer() {
        return this.getClass() == Player.class;
    }
    
    /** Tries to move the Creature according to its current movement flags. */
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
    
    /** Returns whether this Creature is currently trying to move forward. */
    public boolean isGoingForward() {
        return stateFlags.contains(StateFlag.GOING_FORWARD);
    }
    
    /** Causes the Creature to start trying to move forward. */
    public void goForward() {
        stateFlags.add(StateFlag.GOING_FORWARD);
    }
    
    /** Causes the Creature to stop trying to move forward. */
    public void stopGoingForward() {
        stateFlags.remove(StateFlag.GOING_FORWARD);
    }
    
    /** Returns whether this Creature is currently trying to move backward. */
    public boolean isGoingBackward() {
        return stateFlags.contains(StateFlag.GOING_BACKWARD);
    }
    
    /** Causes the Creature to start trying to move backward. */
    public void goBackward() {
        stateFlags.add(StateFlag.GOING_BACKWARD);
    }
    
    /** Causes the Creature to stop trying to move backward. */
    public void stopGoingBackward() {
        stateFlags.remove(StateFlag.GOING_BACKWARD);
    }
    
    /** Returns whether this Creature is currently turning left. */
    public boolean isTurningLeft() {
        return stateFlags.contains(StateFlag.TURNING_LEFT);
    }
    
    /** Causes the Creature to start turning left. */
    public void turnLeft() {
        stateFlags.add(StateFlag.TURNING_LEFT);
    }
    
    /** Causes the Creature to stop turning left. */
    public void stopTurningLeft() {
        stateFlags.remove(StateFlag.TURNING_LEFT);
    }
    
    /** Returns whether this Creature is currently turning right. */
    public boolean isTurningRight() {
        return stateFlags.contains(StateFlag.TURNING_RIGHT);
    }
    
    /** Causes the Creature to start turning right. */
    public void turnRight() {
        stateFlags.add(StateFlag.TURNING_RIGHT);
    }
    
    /** Causes the Creature to stop turning right. */
    public void stopTurningRight() {
        stateFlags.remove(StateFlag.TURNING_RIGHT);
    }
    
    /** Returns whether this Creature can be seen by the given Creature. */
    public boolean canBeSeenBy(Creature creature) {
        return getTileUnder().canBeSeenBy(creature) && !this.isInvisible();
    }
    
    /** Returns Whether this Creature can see the specified point (x, y). */
    public boolean canSee(float x, float y) {
        assert getArea().hasLocation(x / Tile.size, y / Tile.size) : "Not a valid location!";
        
        if (getDistance(this, x, y) > sight * Tile.size) return false;
        
        return area.getTilesOnLine(getX(), getY(), x, y)
                .stream()
                .noneMatch((tile) -> (tile.blocksVision()));
    }
    
    /** Modulate the graphical representation of this Creature by the given
     *  Color. */
    public void setLighting(Color color) {
        sprite.setColor(color);
    }
    
    /** Returns the tile this Creature is currently standing on. */
    public Tile getTileUnder() {
        return area.getTileAt(getX(), getY());
    }
    
    /** Returns the tile this Creature was standing on before moving to its
     *  current tile. */
    public Tile getPreviousTile() {
        return area.getTileAt(previousTileX, previousTileY);
    }
    
    /** Returns the team this Creature belongs to. */
    public Team getTeam() {
        return team;
    }
    
    /** Returns the radius of the circular area that this Creature's unarmed
     *  attacks deal damage to. */
    public int getUnarmedAttackSize() {
        return unarmedAttackSize;
    }
    
    /** Returns whether this Creature is on a different team than the specified
     *  Creature. */
    public boolean isEnemyOf(Creature other) {
        return this.getTeam() != other.getTeam();
    }
    
    /** Returns whether this Creature should pick up items on the ground. */
    public boolean picksUpItems() {
        return stateFlags.contains(StateFlag.PICKS_UP_ITEMS);
    }
    
    //SORT THIS, make comparator
    /** Returns all enemies that this Creature currently sees. */
    public List<Creature> getEnemiesWithinFOV() {
        return getArea().getCreatures()
                .stream()
                .filter((creature) -> (creature.isEnemyOf(this) && creature.canBeSeenBy(this)))
                .collect(Collectors.toList());
    }
    
    /** Returns all allies that this Creature currently sees. */
    public List<Creature> getFriendliesWithinFOV() {
        return getArea().getCreatures()
                .stream()
                .filter((creature) -> (!creature.isEnemyOf(this) && creature.canBeSeenBy(this)))
                .collect(Collectors.toList());
    }
    
    /** Increases this Creature's HP by the given amount. */
    public void addHP(int amount) {
        HP.increase(amount);
        if (this.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) this, Statistic.HEALTHREGAINED, amount);
        }
        
    }
    
    /** Increases this Creature's energy by the given amount. */
    public void addEnergy(int amount) {
        energy.increase(amount);
    }
    
    /** Activates run mode for this Creature. */
    public void startRunning() {
        stateFlags.add(StateFlag.IS_RUNNING);
    }
    
    /** Deactivates run mode for this Creature. */
    public void stopRunning() {
        stateFlags.remove(StateFlag.IS_RUNNING);
    }
    
    /** Returns whether this Creature is currently in run mode. */
    public boolean isRunning() {
        return stateFlags.contains(StateFlag.IS_RUNNING);
    }
    
    /** Decreases the energy of this Creature once. */
    public void consumeEnergy() {
        energy.decrease();
    }
    
    /** Returns whether this Creature has energy left for running. */
    public boolean canRun() {
        return !energy.isEmpty();
    }
    
    /** Returns the x-coordinate of the tile this Creature is currently
     *  standing on. */
    public int getTileX() {
        return floatPosToTilePos(getX());
    }
    
    /** Returns the y-coordinate of the tile this Creature is currently
     *  standing on. */
    public int getTileY() {
        return floatPosToTilePos(getY());
    }
    
    /** Changes the team of this Creature to the specified team. */
    public void setTeam(Team team) {
        this.team = team;
    }
    
    /** Returns whether both this Creature and the specified Creature are
     *  currently standing in the same tile. */
    public boolean isInSameTileAs(Creature other) {
        return this.getTileX() == other.getTileX()
            && this.getTileY() == other.getTileY();
    }
    
    /** Activates/deactivates invisibility for this Creature.
     *  @param truth whether the invisibility mode should be activated */
    public void setInvisibility(boolean truth) {
        if (truth) sprite.setAlpha(0.3f);
        else sprite.setAlpha(1);
        if (truth)
            stateFlags.add(StateFlag.IS_INVISIBLE);
        else
            stateFlags.remove(StateFlag.IS_INVISIBLE);
    }
    
    /** Returns whether this Creature has invisibility currently activated. */
    public boolean isInvisible() {
        return stateFlags.contains(StateFlag.IS_INVISIBLE);
    }
    
    /** Removes the specified item from this Creature's inventory. */
    public void removeItem(Item item) {
        inventory.removeItem(item);
    }
    
    /** Returns whether this Creature is turned towards the target Creature. */
    public boolean isFacing(Creature target) {
        return abs(getDiff(this.getDirection(), target.direction)) < PI/48;
    }
    
    /** Returns whether this Creature has fly mode currently activated. */
    public boolean isFlying() {
        return stateFlags.contains(StateFlag.IS_FLYING);
    }
    
    /** Activates/deactivates flying for this Creature.
     *  @param truth whether the fly mode should be activated */
    public void setFlying(boolean truth) {
        if (truth)
            stateFlags.add(StateFlag.IS_FLYING);
        else
            stateFlags.remove(StateFlag.IS_FLYING);
    }
    
    /** Specifies the position of this Creature's graphical representation in
     *  the main texture file.
     *  @param x the x-coordinate of the sprite
     *  @param y the y-coordinate of the sprite */
    public void setSprite(int x, int y) {
        sprite = new Sprite(texture, x*Tile.size, y*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
    }
    
    /** Returns the current defense attribute of this Creature. */
    public int getDefense() {
        return this.defense;
    }
    
    /** Returns whether this Creature is currently standing on a tile that's
     *  passable, i.e. is not stuck and can move. */
    public boolean isOnPassableObject() {
        return this.getTileUnder().isPassable();
    }
    
    /** Returns the circular area that this Creature occupies. */
    public Circle getBounds() {
        return bounds;
    }
    
    /** Tests whether this Creature would collide with other Creatures if it was
     *  positioned at the the specified point (x, y).
     *  @param x the x-coordinate of the position to test for collision
     *  @param y the y-coordinate of the position to test for collision
     *  @return whether any collision was detected */
    private boolean collisionDetected(float x, float y) {
        Circle newBounds = new Circle(x, y, getSize());
        List<Creature> creatures = area.getCreaturesNear(x, y);
        creatures.remove(this);
        return creatures
                .stream()
                .anyMatch((other) -> (overlaps(newBounds, other.getBounds())));
    }
    
    /** Returns all Creatures that are on the same tile as this Creature or on
     *  any of the 8 neighboring tiles. */
    public List<Creature> getNearbyCreatures() {
        List<Creature> creatures = new ArrayList();
        getTileUnder().getNearbyTiles(true)
                .stream()
                .forEach((tile) -> creatures.addAll(tile.getCreatures()));
        creatures.addAll(getTileUnder().getCreatures());
        creatures.remove(this);
        return creatures;
    }
}
