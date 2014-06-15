package wge3.entity.terrainelement;

public abstract class MapObject extends TerrainElement {

    public MapObject(String texturePath) {
        super(texturePath);
        
        // Default values:
        passable = true;
        blocksVision = true;
        drainsHealth = false;
    }
}
