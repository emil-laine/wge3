package wge3.entity.mapobject;

public class Door extends Wall {
    
    private boolean closed;
    private boolean hasLock;
    private boolean locked;

    // private KeyType keytype;
    
    public Door() {
        super();
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
}
