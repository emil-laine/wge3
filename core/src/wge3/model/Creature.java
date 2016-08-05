/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.radiansToDegrees;
import com.badlogic.gdx.math.Rectangle;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static wge3.engine.PlayState.messageStream;
import wge3.engine.Statistic;
import wge3.engine.util.Config;
import wge3.engine.util.Drawable;
import static wge3.engine.util.Math.getDiff;
import wge3.engine.util.StatIndicator;
import static wge3.model.ai.PathFinder.findPath;
import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static wge3.engine.util.Math.getDistance;

public abstract class Creature extends Entity implements Drawable {
    
    protected Team team;
    
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected float turningSpeed;
    protected int sight;
    
    protected StatIndicator HP;
    protected int strength;
    protected int defense;
    protected int unarmedAttackSize; // radius
    // Regen rates: the amount of milliseconds between regenerating 1 unit.
    protected int HPRegenRate;
    protected long lastHPRegen;
    
    protected Inventory inventory;
    protected Item selectedItem;
    
    private static final Config cfg = new Config("config/creature.toml");
    
    private final boolean isFlying;
    
    public Creature(String type) {
        super((int) (Tile.size * cfg.getFloat(type, "size")));
        defaultSpeed = (int) (Tile.size * cfg.getFloat(type, "speed"));
        currentSpeed = defaultSpeed;
        direction = random() * PI2;
        turningSpeed = (int) (cfg.getFloat(type, "turningSpeed"));
        sight = (int) (Tile.size * cfg.getFloat(type, "sight"));
        unarmedAttackSize = (int) (Tile.size * cfg.getFloat(type, "unarmedAttackSize"));
        
        strength = cfg.getInt(type, "strength");
        defense = cfg.getInt(type, "defense");
        
        HP = new StatIndicator();
        HP.setMax(cfg.getInt(type, "hp"));
        HPRegenRate = cfg.getInt(type, "hpRegenRate");
        lastHPRegen = millis();
        
        inventory = new Inventory(this);
        for (String itemType : cfg.getStringList(type, "inventory")) {
            inventory.addItem(Item.get(itemType));
        }
        changeItem();
        
        isFlying = false; // No flying creatures yet.
        
        setSprite(cfg.getIntX(type, "spritePos")
                  + MathUtils.random(cfg.getInt(type, "spriteMultiplicity") - 1),
                  cfg.getIntY(type, "spritePos"));
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
    
    /** Returns the maximum HP of this Creature. */
    public int getMaxHP() {
        return HP.getMax();
    }
    
    /** Returns the current HP of this Creature. */
    public int getCurrentHP() {
        return HP.getCurrent();
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
    public void update() {
        if (!isGhost() && !isFlying()) {
            dealDamage(getTileUnder().getHPDrainAmount());
        }
        regenerate(millis());
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
            float movementModifier = getArea().getTileAt(getX(), getY()).getMovementModifier();
            dx *= movementModifier;
            dy *= movementModifier;
        }
        
        // Calculate actual movement:
        float destX = getX() + dx;
        float destY = getY() + dy;
        
        if (canMoveTo(destX, destY)) {
            setPosition(destX, destY);
        } else if (canMoveTo(getX(), destY)) {
            setY(destY);
        } else if (canMoveTo(destX, getY())) {
            setX(destX);
        }
        
        // These could be optimized to be checked less than FPS times per second:
        if (hasMovedToANewTile()) {
            getTileUnder().enter(this, getPreviousTile());
            setPreviousTilePosition(getTileX(), getTileY());
            if (picksUpItems()) pickUpItems();
        }
    }
    
    /** Removes any items this Creature is touching, and adds them
     *  to the Creature's inventory. */
    public void pickUpItems() {
        for (ItemInstance item : getArea().getItemsWithin(getBounds())) {
            if (!item.canBePickedUp()) continue;
            inventory.addItem(item.getType());
            getArea().removeEntity(item);
            incrementStat(Statistic.ITEMS_PICKED_UP, 1);
        }
    }
    
    /** Returns whether this Creature can move to the specified point (x, y). */
    public boolean canMoveTo(float x, float y) {
        Rectangle newBounds = new Rectangle(x - getSize()/2, y - getSize()/2, getSize(), getSize());
        
        if (!getArea().getBounds().contains(newBounds))
            return false;
        
        if (this.isGhost())
            return true;
        
        List<Tile> destTiles = getArea().getOverlappingTiles(newBounds);
        
        if (getTileUnder().preventsMovement(this, x - getX(), y - getY()))
            return false;
        
        if (collisionDetected(newBounds))
            return false;
        
        if (!this.isOnPassableObject())
            return true;
        
        return destTiles.stream().allMatch(Tile::isPassable);
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
            incrementStat(Statistic.ITEMS_USED, 1);
        }
    }
    
    /** Equips the next item from inventory. */
    public void changeItem() {
        setSelectedItem(inventory.getNextItem());
    }
    
    public void drop(ItemInstance toDrop) {
        removeItem(toDrop);
        getArea().addEntity(toDrop, getX(), getY());
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
        return getStateFlags().contains(StateFlag.SEES_EVERYTHING);
    }
    
    /** Changes the state of the SEES_EVERYTHING flag. */
    public void toggleSeeEverything() {
        if (getStateFlags().contains(StateFlag.SEES_EVERYTHING)) {
            getStateFlags().remove(StateFlag.SEES_EVERYTHING);
        } else {
            getStateFlags().add(StateFlag.SEES_EVERYTHING);
            getArea().getTiles().stream().forEach((tile) -> tile.setLighting(Color.WHITE));
        }
    }
    
    /** Returns whether this Creature is a ghost. */
    public boolean isGhost() {
        return getStateFlags().contains(StateFlag.IS_GHOST);
    }
    
    /** Changes the state of the IS_GHOST flag. */
    public void toggleGhostMode() {
        if (getStateFlags().contains(StateFlag.IS_GHOST)) {
            getStateFlags().remove(StateFlag.IS_GHOST);
            messageStream.addMessage("Ghost Mode Off");
        }
        else {
            getStateFlags().add(StateFlag.IS_GHOST);
            messageStream.addMessage("Ghost Mode On");
        }
    }
    
    /** Deals damage to this Creature.
     *  @param amount how much damage */
    public void dealDamage(int amount) {
        int decreaseAmount = max(amount - defense, 0);
        HP.decrease(decreaseAmount);
        incrementStat(Statistic.DAMAGE_TAKEN, decreaseAmount);
    }
    
    /** Returns whether this Creature is dead. */
    public boolean isDead() {
        return HP.isEmpty();
    }
    
    /** Regenerates the HP of this Creature, if needed.
     *  @param currentTime the current time, used to decide whether regeneration
     *                     is needed */
    public void regenerate(long currentTime) {
        if (currentTime - lastHPRegen > HPRegenRate) {
            regenerateHP();
            lastHPRegen = currentTime;
        }
    }
    
    /** Regenerates the HP of this Creature. */
    private void regenerateHP() {
        HP.increase();
        incrementStat(Statistic.HEALTH_REGAINED, 1);
    }
    
    /** Rotates the graphical representation of this Creature according to
     *  the direction the Creature is currently facing. */
    public void updateSpriteRotation() {
        getSprite().setRotation(direction * radiansToDegrees);
    }
    
    /** Performs an unarmed attack targeted at a circular area directly in front
     *  of the Creature. */
    public void attackUnarmed() {
        float destX = getX() + MathUtils.cos(direction) * Tile.size;
        float destY = getY() + MathUtils.sin(direction) * Tile.size;
        Circle dest = new Circle(destX, destY, getUnarmedAttackSize());
        for (Creature creature : getArea().getCreatures()) {
            if (dest.contains(creature.getX(), creature.getY())) {
                if (creature.getTeam() != getTeam()) {
                    creature.dealDamage(strength);
                    incrementStat(Statistic.DAMAGE_DEALT, strength);
                }
            }
        }
        getArea().getTileAt(destX, destY).dealDamage(this.strength);
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
        return getStateFlags().contains(StateFlag.GOING_FORWARD);
    }
    
    /** Causes the Creature to start trying to move forward. */
    public void goForward() {
        getStateFlags().add(StateFlag.GOING_FORWARD);
    }
    
    /** Causes the Creature to stop trying to move forward. */
    public void stopGoingForward() {
        getStateFlags().remove(StateFlag.GOING_FORWARD);
    }
    
    /** Returns whether this Creature is currently trying to move backward. */
    public boolean isGoingBackward() {
        return getStateFlags().contains(StateFlag.GOING_BACKWARD);
    }
    
    /** Causes the Creature to start trying to move backward. */
    public void goBackward() {
        getStateFlags().add(StateFlag.GOING_BACKWARD);
    }
    
    /** Causes the Creature to stop trying to move backward. */
    public void stopGoingBackward() {
        getStateFlags().remove(StateFlag.GOING_BACKWARD);
    }
    
    /** Returns whether this Creature is currently turning left. */
    public boolean isTurningLeft() {
        return getStateFlags().contains(StateFlag.TURNING_LEFT);
    }
    
    /** Causes the Creature to start turning left. */
    public void turnLeft() {
        getStateFlags().add(StateFlag.TURNING_LEFT);
    }
    
    /** Causes the Creature to stop turning left. */
    public void stopTurningLeft() {
        getStateFlags().remove(StateFlag.TURNING_LEFT);
    }
    
    /** Returns whether this Creature is currently turning right. */
    public boolean isTurningRight() {
        return getStateFlags().contains(StateFlag.TURNING_RIGHT);
    }
    
    /** Causes the Creature to start turning right. */
    public void turnRight() {
        getStateFlags().add(StateFlag.TURNING_RIGHT);
    }
    
    /** Causes the Creature to stop turning right. */
    public void stopTurningRight() {
        getStateFlags().remove(StateFlag.TURNING_RIGHT);
    }
    
    /** Returns Whether this Creature can see the specified point (x, y). */
    public boolean canSee(float x, float y) {
        assert getArea().hasLocation(x / Tile.size, y / Tile.size) : "Not a valid location!";
        
        if (getDistance(this, x, y) > sight * Tile.size) return false;
        
        return getArea().getTilesOnLine(getX(), getY(), x, y)
                .stream()
                .noneMatch((tile) -> (tile.blocksVision()));
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
        return getStateFlags().contains(StateFlag.PICKS_UP_ITEMS);
    }
    
    // TODO: Sort this, make comparator
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
        final int healedAmount = getCurrentHP() >= amount ? amount : (getMaxHP() - getCurrentHP());
        incrementStat(Statistic.HEALTH_REGAINED, healedAmount);
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
    
    /** Removes the specified item from this Creature's inventory. */
    public void removeItem(ItemInstance item) {
        inventory.removeItem(item.getType());
    }
    
    /** Returns whether this Creature is turned towards the target Creature. */
    public boolean isFacing(Creature target) {
        return abs(getDiff(this.getDirection(), target.direction)) < PI/48;
    }
    
    /** Returns whether this is a flying Creature. */
    public boolean isFlying() {
        return isFlying;
    }
    
    @Override
    public void setSprite(int x, int y) {
        super.setSprite(x, y);
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
    
    /** Tests whether this Creature would collide with other Creatures
     *  if it had the specified bounds.
     *  @param targetBounds the rectangular area to test for collision
     *  @return whether any collision was detected */
    private boolean collisionDetected(Rectangle targetBounds) {
        for (Tile tile : getArea().getOverlappingTiles(targetBounds)) {
            for (Creature creature : tile.getCreatures()) {
                if (creature == this)
                    continue;
                if (creature.getBounds().overlaps(targetBounds))
                    return true;
            }
        }
        return false;
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
    
    public void die() {
        ItemInstance item = new ItemInstance(getInventory().getRandomItem());
        if (item != null) {
            getArea().addEntity(item, getX(), getY());
            getInventory().removeAll();
        }
    }
    
    protected static final Config getCfg() {
        return cfg;
    }
}
