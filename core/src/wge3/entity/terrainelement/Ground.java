package wge3.entity.terrainelement;

public abstract class Ground extends TerrainElement {
    
    public Ground(String texturePath) {
        super(texturePath);
        
        // Default values:
        affectsMovement = false;
    }
}
