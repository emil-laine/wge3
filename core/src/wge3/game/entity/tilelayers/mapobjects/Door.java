package wge3.game.entity.tilelayers.mapobjects;

import java.util.EnumSet;
import static wge3.game.engine.constants.TilePropertyFlag.BLOCKS_VISION;
import static wge3.game.engine.constants.TilePropertyFlag.IS_INDOORS;
import static wge3.game.engine.constants.TilePropertyFlag.IS_PASSABLE;

public final class Door extends Wall {
    
    private int destroyThreshold;
    private boolean horizontal;
    
    // private boolean locked;  -> DoorWithLock class
    // private KeyType keytype; -> DoorWithLock class
    
    public Door(boolean horizontal, boolean closed) {
        setSprite(horizontal? 7:6, closed? 3:4);
        destroyThreshold = 50;
        this.horizontal = horizontal;
        propertyFlags = EnumSet.of(IS_INDOORS,
                closed? BLOCKS_VISION : IS_PASSABLE);
    }
    
    public boolean isClosed() {
        return !propertyFlags.contains(IS_PASSABLE);
    }
    
    public void close() {
        propertyFlags.remove(IS_PASSABLE);
        propertyFlags.add(BLOCKS_VISION);
        changeSprite();
    }
    
    public void open() {
        propertyFlags.add(IS_PASSABLE);
            propertyFlags.remove(BLOCKS_VISION);
            changeSprite();
    }

    @Override
    public void dealDamage(int amount) {
        if (amount >= destroyThreshold)
            HP = 0;
        else
            if (isClosed()) open();
            else close();
    }
    
    public void changeSprite() {
        setSprite(horizontal? 7:6, isClosed()? 3:4);
    }
}
