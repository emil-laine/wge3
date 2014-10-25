package wge3.game.entity.tilelayers.mapobjects;

import static com.badlogic.gdx.math.MathUtils.random;
import java.util.Iterator;
import java.util.List;
import wge3.game.engine.constants.TilePropertyFlag;
import wge3.game.entity.tilelayers.MapObject;
import wge3.game.entity.Tile;

public class GreenSlime extends MapObject {

    private int damage;
    public static float expansionProbability = 1f/64f;
    
    public GreenSlime() {
        setSprite(1, 4);
        propertyFlags.remove(TilePropertyFlag.IS_PASSABLE);
        propertyFlags.remove(TilePropertyFlag.BLOCKS_VISION);
        propertyFlags.remove(TilePropertyFlag.COVERS_WHOLE_TILE);
        HP = 100;
        hardness = 10;
        damage = 20;
    }
    
    public GreenSlime expand() {
        Tile target = getExpansionTarget();
        if (target == null) return null;
        
        if ((!target.hasObject() && !target.hasCreature())
                || (target.hasObject() && target.isPassable())) {
            GreenSlime newSlime = new GreenSlime();
            target.setObject(newSlime);
            return newSlime;
        }
        
        target.dealDamage(damage);
        return null;
    }
    
    public Tile getExpansionTarget() {
        List<Tile> tiles = tile.getNearbyTiles();
        for (Iterator<Tile> it = tiles.iterator(); it.hasNext();) {
            Tile tile = it.next();
            if (tile.hasCreature() || tile.hasSlime())
                it.remove();
        }
        if (tiles.isEmpty()) return null;
        return tiles.get(random(tiles.size()-1));
    }
}
