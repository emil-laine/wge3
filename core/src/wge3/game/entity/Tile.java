package wge3.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wge3.game.engine.gui.Drawable;
import wge3.game.entity.bombs.Bomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.tilelayers.MapObject;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.tilelayers.mapobjects.Teleport;
import wge3.game.entity.tilelayers.mapobjects.Tree;

public class Tile implements Drawable {
    
    public static final int size = 24;
    
    private static Area area;
    private int x, y; // in tiles
    private Rectangle bounds;
    
    private Ground ground;
    private MapObject object;
    
    public Tile() {
        bounds = new Rectangle();
        bounds.width = Tile.size;
        bounds.height = Tile.size;
    }
    
    /** Returns the Ground layer of this Tile. */
    public Ground getGround() {
        return ground;
    }
    
    /** Returns the object on top of the ground, or null if there's no object. */
    public MapObject getObject() {
        return object;
    }
    
    /** Changes the ground of this Tile to the specified Ground.
     *  @param g non-null */
    public void setGround(Ground g) {
        g.setTile(this);
        g.setPosition(x * Tile.size, y * Tile.size);
        ground = g;
    }
    
    /** Adds the given object to this Tile. Any pre-existing objects on this
     *  Tile will be lost.
     *  @param the non-null object to add onto this tile */
    public void setObject(MapObject o) {
        o.setTile(this);
        o.setPosition(x * Tile.size, y * Tile.size);
        object = o;
    }
    
    /** Deletes the current object (if any) on this Tile. */
    public void removeObject() {
        object = null;
    }
    
    /** Returns whether this tile is walkable. */
    public boolean isPassable() {
        if (object == null) {
            return true;
        } else {
            return object.isPassable();
        }
    }
    
    /** Returns the x-coordinate of this Tile in the tile grid. */
    public int getX() {
        return x;
    }
    
    /** Returns the y-coordinate of this Tile in the tile grid. */
    public int getY() {
        return y;
    }
    
    /** Sets the area this Tile belongs in. */
    public static void setArea(Area area) {
        Tile.area = area;
    }
    
    /** Returns the Area this Tile belongs in. */
    public static Area getArea() {
        return area;
    }
    
    /** Returns the rectangular area this Tile occupies. */
    public Rectangle getBounds() {
        return bounds;
    }
    
    /** Sets the location of this Tile on the tile grid of area.
     *  @param x the x-coordinate in the tile grid
     *  @param y the y-coordinate in the tile grid */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        bounds.x = x * Tile.size;
        bounds.y = y * Tile.size;
        ground.setPosition(x * Tile.size, y * Tile.size);
        if (hasObject()) {
            object.setPosition(x * Tile.size, y * Tile.size);
        }
    }
    
    /** Changes the amount and color of light in this Tile according to the
     *  given color.
     *  @param color the color used to modulate the lightning. A full white
     *               color would cause the tile to be fully lit, while a full
     *               black color would make it completely dark. */
    public void setLighting(Color color) {
        ground.setLighting(color);
        if (hasObject()) object.setLighting(color);
        getCreatures()
                .stream()
                .filter((creature) -> (!creature.isInvisible()))
                .forEach((creature) -> creature.setLighting(color));
        getBombs()
                .stream()
                .forEach((bomb) -> bomb.setLighting(color));
    }
    
    /** Returns the multiplier that tells how this Tile affects movement speed.
     *  @return 1.0 means this Tile doesn't affect movement. Values greater than
     *          1.0 mean movement will be sped up, while values lower 1.0 mean
     *          movement will be slowed down. 0.0 will stop all movement, and
     *          negative values will reverse movement direction. */
    public float getMovementModifier() {
        if (!hasObject()) {
            return ground.getMovementModifier();
        } else {
            return ground.getMovementModifier() * object.getMovementModifier();
        }
    }
    
    /** Returns whether this Tile has a solid object on it that can't be seen
     *  through. */
    public boolean blocksVision() {
        if (object != null) {
            return object.blocksVision();
        } else {
            return false;
        }
    }
    
    @Override
    public void draw(Batch batch) {
        if (!hasObject() || object.isTree()) {
            ground.draw(batch);
        } else if (object.coversWholeTile()) {
            object.draw(batch);
        } else {
            ground.draw(batch);
            batch.enableBlending();
            object.draw(batch);
            batch.disableBlending();
        }
    }
    
    /** Returns whether the given Creature can see this Tile. */
    public boolean canBeSeenBy(Creature creature) {
        return canBeSeenFrom(creature.getX(), creature.getY(), creature.getSight(), creature.isFlying()) || creature.seesEverything();
    }
    
    /** Returns whether a Creature with the given sight would be able to see
     *  this Tile from the given position (x, y). If aerial is true, then the
     *  Creature is considered to be flying, i.e. it can see over walls, etc. */
    public boolean canBeSeenFrom(float x, float y, int sight, boolean aerial) {
        assert getArea().hasLocation(x, y) : "Not a valid location!";
        
        if (getDistanceTo(x, y) > sight * Tile.size) return false;
        
        return area.getTilesOnLine(x, y, getMiddleX(), getMiddleY())
                .stream()
                .noneMatch((tile) -> (tile.blocksVision() && !aerial));
    }
    
    /** Calls {@link #canBeSeenFrom(float, float, int, boolean)}
     *  with aerial set to false. */
    public boolean canBeSeenFrom(float x, float y, int range) {
        return canBeSeenFrom(x, y, range, false);
    }
    
    /** Calculates the distance from the middlepoint of this Tile to the given
     *  point (x, y). */
    public float getDistanceTo(float x, float y) {
        float dx = x - this.getMiddleX();
        float dy = y - this.getMiddleY();
        return (float) Math.sqrt(dx*dx + dy*dy);
    }
    
    /** Calculates the squared distance from the middlepoint of this Tile to the
     *  given point (x, y). */
    public float getDistance2To(float x, float y) {
        float dx = x - this.getMiddleX();
        float dy = y - this.getMiddleY();
        return (dx * dx) + (dy * dy);
    }
    
    /** Returns whether there's currently an object on this Tile. */
    public boolean hasObject() {
        return object != null;
    }
    
    /** Returns whether there's currently an item on this Tile. */
    public boolean hasItem() {
        if (object == null) return false;
        return object.isItem();
    }
    
    /** Returns whether there's currently a Creature standing on this Tile. */
    public boolean hasCreature() {
        return !getCreatures().isEmpty();
    }
    
    /** Returns all creatures on this Tile. */
    public List<Creature> getCreatures() {
        return area.getCreatures()
                .stream()
                .filter((c) -> (c.getTileX() == getX() && c.getTileY() == getY()))
                .collect(Collectors.toList());
    }
    
    /** Returns all bomb objects on this Tile. */
    public List<Bomb> getBombs() {
        return area.getBombs()
                .stream()
                .filter((bomb) -> (area.getTileAt(bomb.getX(), bomb.getY()).equals(this)))
                .collect(Collectors.toList());
    }
    
    /** Deals damage to all objects and Creatures on this Tile. */
    public void dealDamage(int amount) {
        for (Creature creature : getCreatures()) {
            creature.dealDamage(amount);
            if (creature.isDead()) {
                creature = null;
            }
        }
        if (hasItem()) {
            object = null; // Items don't take damage, at least for now.
        } else if (hasObject()) {
            object.dealDamage(amount);
            if (object.isDestroyed() & !object.hasDestroyedSprite()) {
                object = null;
            }
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    /** Returns whether standing on this Tile causes a Creature's HP to
     *  decrease. */
    public boolean drainsHP() {
        if (object == null) {
            return ground.drainsHP();
        } else {
            return object.drainsHP();
        }
    }
    
    /** Returns the amount of HP that standing on this Tile causes a Creature to
     *  lose when unprotected. */
    public int getHPDrainAmount() {
        if (object == null) {
            return ground.getHPDrainAmount();
        } else {
            return object.getHPDrainAmount();
        }
    }
    
    /** Tells the game that this Tile should be redrawn. */
    public void requestDraw() {
        area.addTileToDraw(this);
        if (hasTree())
            area.addTreeToDraw((Tree) object);
    }
    
    /** Returns whether the given Creature can safely move to this Tile from its
     *  current position in a straight line. */
    public boolean isAnOKMoveDestinationFor(Creature creature) {
        return isAnOKMoveDestinationFrom(creature.getX(), creature.getY());
    }
    
    /** Returns whether a Creature could safely move to this Tile in a straight
     *  line from the given position (startX, startY). */
    public boolean isAnOKMoveDestinationFrom(float startX, float startY) {
        if (!isGoodMoveDest()) return false;
        
        // Don't call tile.blocksVision() here;
        // it must be called before calling this method.
        return getArea().getTilesOnLine(startX, startY, getMiddleX(), getMiddleY())
                .stream()
                .noneMatch((tile) -> (!tile.isGoodMoveDest()));
    }
    
    /** Returns whether this Tile is a sensible location to move to for a
     *  non-ghost non-flying Creature. */
    public boolean isGoodMoveDest() {
        return isPassable() && !drainsHP();
    }
    
    /** Returns the x-coordinate of the left border of this Tile. */
    public int getLeftX() {
        return getX() * Tile.size;
    }
    
    /** Returns the x-coordinate of the right border of this Tile. */
    public int getRightX() {
        return (getX() + 1) * Tile.size;
    }
    
    /** Returns the y-coordinate of the bottom border of this Tile. */
    public int getBottomY() {
        return getY() * Tile.size;
    }
    
    /** Returns the y-coordinate of the top border of this Tile. */
    public int getTopY() {
        return (getY() + 1) * Tile.size;
    }
    
    /** Returns the x-coordinate of the middle point of this Tile. */
    public int getMiddleX() {
        return getX()*Tile.size + Tile.size/2;
    }
    
    /** Returns the y-coordinate of the middle point of this Tile. */
    public int getMiddleY() {
        return getY()*Tile.size + Tile.size/2;
    }
    
    /** Returns whether this Tile only allows movement to one direction. */
    public boolean isOneWay() {
        return getGround().getClass() == OneWayFloor.class;
    }
    
    /** Returns whether this Tile has a slime on it. */
    public boolean hasSlime() {
        if (!hasObject()) return false;
        return object.isSlime();
    }
    
    /** Returns whether this Tile has a tree on it. */
    public boolean hasTree() {
        if (!hasObject()) return false;
        return object.isTree();
    }
    
    /** Returns all neighboring tiles. This includes tiles to the north, east,
     *  west, and south of this Tile (if they exist). If includeDiagonal is
     *  true, also tiles to the north-east, south-east, south-west, and
     *  north-west are included (if they exist). */
    public List<Tile> getNearbyTiles(boolean includeDiagonal) {
        List<Tile> tiles = new ArrayList<>();
        if (area.hasLocation(x-1, y)) tiles.add(area.getTileAt(x-1, y));
        if (area.hasLocation(x+1, y)) tiles.add(area.getTileAt(x+1, y));
        if (area.hasLocation(x, y-1)) tiles.add(area.getTileAt(x, y-1));
        if (area.hasLocation(x, y+1)) tiles.add(area.getTileAt(x, y+1));
        if (includeDiagonal) {
            if (area.hasLocation(x-1, y-1)) tiles.add(area.getTileAt(x-1, y-1));
            if (area.hasLocation(x+1, y+1)) tiles.add(area.getTileAt(x+1, y+1));
            if (area.hasLocation(x+1, y-1)) tiles.add(area.getTileAt(x+1, y-1));
            if (area.hasLocation(x-1, y+1)) tiles.add(area.getTileAt(x-1, y+1));
        }
        return tiles;
    }
    
    /** Returns whether this Tile has an object on it that casts shadows. */
    public boolean castsShadows() {
        if (!hasObject()) return false;
        return object.castsShadows();
    }
    
    /** Creates a new GreenSlime to this Tile. */
    public void addSlime() {
        GreenSlime newSlime = new GreenSlime();
        this.setObject(newSlime);
        area.addSlime(newSlime);
    }
    
    /** Returns whether this Tile has a Teleport on it. */
    public boolean hasTeleport() {
        if (!hasObject()) return false;
        return object.isTeleport();
    }
    
    /** Returns the Teleport object on this Tile. Only call this if you know
     *  that this Tile has a Teleport, e.g. after a call to hasTeleport(). */
    public Teleport getTeleport() {
        return (Teleport) object;
    }
}
