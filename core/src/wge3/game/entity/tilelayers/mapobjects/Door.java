package wge3.game.entity.tilelayers.mapobjects;

import com.badlogic.gdx.graphics.g2d.Batch;

public final class Door extends Wall {
    
    private boolean closed;
    private boolean hasLock;
    private boolean locked;

    // private KeyType keytype;
    
    public Door() {
    }
    
    @Override
    public boolean isPassable() {
        return !closed;
    }
    
    public void close() {
        closed = true;
    }
    
    public void open() {
        if (!locked) {
            closed = false;
        }
    }
    
    public void lock() {
        if (!closed) {
            close();
        }
        
        locked = true;
    }
    
    public void unlock() {
        locked = false;
    }
    
    @Override
    public boolean blocksVision() {
        return closed;
    }
    
    @Override
    public void draw(Batch batch) {
        if (closed) {
            // draw closed door
        } else {
            // draw open door
        }
    }
}
