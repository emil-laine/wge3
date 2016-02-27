/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

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
import static java.lang.Math.abs;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static wge3.engine.util.Direction.*;
import wge3.engine.Statistic;
import static wge3.engine.PlayState.mStream;
import wge3.engine.util.Drawable;
import static wge3.engine.util.Math.floatPosToTilePos;
import static wge3.engine.util.Math.getDiff;
import static wge3.engine.util.Math.getDistance;
import wge3.engine.util.StatIndicator;
import wge3.model.actors.Player;
import static wge3.model.ai.PathFinder.findPath;
import wge3.model.grounds.OneWayFloor;
import wge3.model.objects.Item;
import wge3.model.objects.Teleport;
import wge3.engine.util.Direction;

public abstract class Creature implements Drawable {
    
    protected Area area;
    protected int previousTileX;
    protected int previousTileY;
    protected Rectangle bounds;
    
    protected Team team;
    
    protected int size;
    protected int defaultSpeed;
    protected int currentSpeed;
    private Direction currentDir;
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
    
    protected final static Texture texture = new Texture(Gdx.files.internal("graphics/graphics.png"));
    protected Sprite sprite;
    
    protected Set<StateFlag> stateFlags;
    private final boolean isFlying;
    
    public Creature() {
        size = Tile.size / 3;
        defaultSpeed = Tile.size * 5;
        currentSpeed = defaultSpeed;
        direction = random() * PI2;
        turningSpeed = 4;
        sight = 12;
        unarmedAttackSize = Tile.size/2;
        
        HP = new StatIndicator();
        HPRegenRate = 1000;
        lastHPRegen = millis();
        
        bounds = new Rectangle(0, 0, size, size);
        
        inventory = new Inventory(this);
        selectedItem = null;
        
        stateFlags = EnumSet.noneOf(StateFlag.class);
        isFlying = false; // No flying creatures yet.
    }
    
    /** Returns the current x position of this Creature. */
    public float getX() {
        return bounds.x + getSize()/2;
    }
    
    public void setX(float x) {
        bounds.setX(x - getSize()/2);
        updateSpritePosition();
    }
    
    /** Returns the current y position of this Creature. */
    public float getY() {
        return bounds.y + getSize()/2;
    }
    
    public void setY(float y) {
        bounds.setY(y - getSize()/2);
        updateSpritePosition();
    }
    
    /** Moves the Creature to the given position (x, y). */
    public void setPosition(float x, float y) {
        bounds.setPosition(x - getSize()/2, y - getSize()/2);
        updateSpritePosition();
    }
    
    /** Moves the Creature to the middle of the tile located at (x, y). */
    public void setPosition(int x, int y) {
        bounds.setPosition(x * Tile.size + Tile.size/2 - getSize()/2,
                           y * Tile.size + Tile.size/2 - getSize()/2);
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
    
    /** Returns the maximum HP of this Creature. */
    public int getMaxHP() {
        return HP.getMax();
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
        Rectangle newBounds = new Rectangle(x - getSize()/2, y - getSize()/2, getSize(), getSize());
        
        if (!area.getBounds().contains(newBounds))
            return false;
        
        if (this.isGhost())
            return true;
        
        List<Tile> destTiles = area.getOverlappingTiles(newBounds);
        
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
        if (currentDir == null) return;
        sprite.setRotation(currentDir.getAngleRad() * MathUtils.radiansToDegrees);
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
        if (currentDir == null) return;
        move(currentDir.getDx() * currentSpeed * delta,
             currentDir.getDy() * currentSpeed * delta);
    }
    
    public void go(Direction where) {
        if (where == currentDir) return;
        currentDir = where;
        updateSpriteRotation();
    }
    
    public void stopGoing() {
        currentDir = null;
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
        if (this.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) this, Statistic.HEALTHREGAINED, amount);
        }
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
    
    /** Returns whether this is a flying Creature. */
    public boolean isFlying() {
        return isFlying;
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
    
    /** Returns the rectangular area that this Creature occupies. */
    public Rectangle getBounds() {
        return bounds;
    }
    
    /** Tests whether this Creature would collide with other Creatures
     *  if it had the specified bounds.
     *  @param targetBounds the rectangular area to test for collision
     *  @return whether any collision was detected */
    private boolean collisionDetected(Rectangle targetBounds) {
        for (Tile tile : area.getOverlappingTiles(targetBounds)) {
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
        Item item = getInventory().getRandomItem();
        if (item != null) {
            getTileUnder().setObject(item);
            getInventory().removeAll();
        }
    }
}