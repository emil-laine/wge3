package wge3.game.entity.tilelayers;

public abstract class Ground extends TileLayer {
    
    protected boolean isIndoors;
    
    public Ground() {
        isIndoors = false;
    }

    public boolean isIndoors() {
        return isIndoors;
    }
}
