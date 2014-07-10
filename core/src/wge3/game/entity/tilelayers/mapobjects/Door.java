package wge3.game.entity.tilelayers.mapobjects;

public final class Door extends Wall {
    
    private boolean closed;
    private boolean hasLock;
    private boolean locked;
    private int destroyThreshold;
    private boolean horizontal;
    
    // private KeyType keytype; -> door with lock
    
    public Door(boolean horizontal, boolean closed) {
        setSprite(horizontal? 7:6, closed? 3:4);
        destroyThreshold = 50;
        this.closed = closed;
        locked = false;
        this.horizontal = horizontal;
        coversWholeTile = false;
    }
    
    @Override
    public boolean isPassable() {
        return !closed;
    }
    
    public void close() {
        closed = true;
        changeSprite();
    }
    
    public void open() {
        if (!locked) {
            closed = false;
            changeSprite();
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
    public void dealDamage(int amount) {
        if (amount >= destroyThreshold) {
            HP = 0;
        }
        if (closed) {
            open();
            return;
        }
        close();
    }
    
    public void changeSprite() {
        setSprite(horizontal? 7:6, closed? 3:4);
    }
}
