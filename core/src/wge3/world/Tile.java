package wge3.world;

import wge3.entity.terrainelements.MapObject;
import wge3.entity.terrainelements.Ground;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.LinkedList;
import java.util.List;
import wge3.entity.character.Creature;
import wge3.interfaces.Drawable;

public class Tile implements Drawable {
    
    public static final int size = 24;
    
    private Area area;
    private int x, y; // in tiles
    private Rectangle bounds;
    
    private Ground ground;
    private MapObject object;

    public Tile() {
        bounds = new Rectangle();
        bounds.width = Tile.size;
        bounds.height = Tile.size;
    }

    public Ground getGround() {
        return ground;
    }

    public MapObject getObject() {
        return object;
    }
    
    public void setGround(Ground g) {
        g.setTile(this);
        g.setPosition(x * Tile.size, y * Tile.size);
        ground = g;
    }

    public void setObject(MapObject o) {
        o.setTile(this);
        o.setPosition(x * Tile.size, y * Tile.size);
        object = o;
    }

    public void removeObject() {
        object = null;
    }

    public boolean isPassable() {
        if (object == null) {
            return true;
        } else {
            return object.isPassable();
        }
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        bounds.x = x * Tile.size;
        bounds.y = y * Tile.size;
        ground.setPosition(x * Tile.size, y * Tile.size);
        if (object != null) {
            object.setPosition(x * Tile.size, y * Tile.size);
        }
    }

    public void setLighting(Color color) {
        ground.setLighting(color);
        if (object != null) object.setLighting(color);
    }
    
    public float getMovementModifier() {
        if (object == null) {
            return ground.getMovementModifier();
        } else {
            return ground.getMovementModifier() * object.getMovementModifier();
        }
    }
    
    public boolean blocksVision() {
        if (object != null) {
            return object.blocksVision();
        } else {
            return false;
        }
    }
    
    @Override
    public void draw(Batch batch) {
        if (object == null) {
            ground.draw(batch);
        } else if (object.blocksVision()) {
            object.draw(batch);
        } else {
            ground.draw(batch);
            batch.enableBlending();
            object.draw(batch);
            batch.disableBlending();
        }
    }
    
    public boolean canBeSeenBy(Creature creature) {
        return canBeSeenFrom(creature.getX(), creature.getY(), creature.getSight());
    }
    
    public boolean canBeSeenFrom(float x, float y, int range) {
        // range: in tiles
        int tileX = getX()*Tile.size + Tile.size/2;
        int tileY = getY()*Tile.size + Tile.size/2;
        float dx = x - tileX;
        float dy = y - tileY;
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
        if (distance > range * Tile.size) {
            return false;
        }
        distance /= Tile.size;
        
        for (int i = 1; i <= distance; i++) {
            if (area.getTileAt(tileX + i*(dx/distance), tileY + i*(dy/distance)).blocksVision()) {
                return false;
            }
        }
        
        return true;
    }
    
    public float getDistanceTo(float x, float y) {
        float dx = x - this.getX()*Tile.size + Tile.size/2;
        float dy = y - this.getY()*Tile.size + Tile.size/2;
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    public boolean hasObject() {
        return object != null;
    }

    public boolean hasItem() {
        if (object == null) return false;
        return object.isItem();
    }

    public boolean hasCreature() {
        return !getCreatures().isEmpty();
    }

    public List<Creature> getCreatures() {
        List<Creature> creatures = new LinkedList<Creature>();
        for (Creature creature : area.getCreatures()) {
            if (area.getTileAt(creature.getX(), creature.getY()).equals(this)) {
                creatures.add(creature);
            }
        }
        return creatures;
    }

    public void dealDamage(int amount) {
        for (Creature creature : getCreatures()) {
            creature.dealDamage(amount);
            if (creature.isDead()) {
                creature = null;
            }
        }
        if (hasItem()) {
            object = null;
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
}
