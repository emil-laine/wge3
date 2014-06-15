package wge3.entity.terrainelement;

public abstract class MapObject extends TerrainElement {

    public MapObject() {
        
        // Default values:
        passable = true;
        blocksVision = true;
        drainsHealth = false;
    }
}
