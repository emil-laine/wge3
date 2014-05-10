package wge3.entity.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.entity.object.Item;
import wge3.entity.object.TimedBomb;
import wge3.world.Area;
import wge3.world.Tile;

public abstract class Character {

    private float x, y;

    protected Area area;
    protected int size;
    protected Color color;
    protected int defaultSpeed;
    protected int currentSpeed;
    protected float direction;
    protected int turningSpeed;

    protected int maxHealth;
    protected int health;
    
    protected Inventory inventory;
    protected Item selectedItem;

    public Character(Area area) {
        this.area = area;
        size = Tile.size;
        defaultSpeed = 100;
        currentSpeed = defaultSpeed;
        x = 400;
        y = 500;
        direction = 0f;
        turningSpeed = 4;
        
        inventory = new Inventory();
        // selectedItem = 
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

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getTurningSpeed() {
        return turningSpeed;
    }

    public void setTurningSpeed(int turningSpeed) {
        this.turningSpeed = turningSpeed;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x - Tile.size / 2, y - Tile.size / 2, Tile.size, Tile.size, Tile.size / 2, Tile.size / 2, direction * 57.2957795131f);
        sr.end();
    }

    public void turnLeft(float delta) {
        direction += turningSpeed * delta;
    }

    public void turnRight(float delta) {
        direction -= turningSpeed * delta;
    }

    public void move(float dx, float dy) {
        int addx = Tile.size / 2;
        int addy = Tile.size / 2;

        if (dx < 0) {
            addx = -addx;
        }

        if (dy < 0) {
            addy = -addy;
        }

        do {
            if (canMove(dx + addx, dy + addy)) {
                x += dx;
                y += dy;
                return;
            } else {
                if (dx >= 1) {
                    dx--;
                } else if (dx <= -1) {
                    dx++;
                } else {
                    dx = 0;
                }

                if (dy >= 1) {
                    dy--;
                } else if (dy <= -1) {
                    dy++;
                } else {
                    dy = 0;
                }
            }
        } while (dx != 0 || dy != 0);
    }

    public boolean canMove(float dx, float dy) {
        if (x + dx >= area.getWidth() || x + dx < 0) {
            return false;
        } else if (y + dy >= area.getHeight() || y + dy < 0) {
            return false;
        }
        
        return area.getTileAt(x + dx, y + dy).isPassable();
    }

    public boolean canSee() {
        return true;
    }
    
    public void useItem() {
        selectedItem.use();
    }
}
