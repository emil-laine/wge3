package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.random;
import java.util.Iterator;
import java.util.List;
import wge3.entity.terrainelements.MapObject;
import wge3.world.Tile;

public class GreenSlime extends MapObject {

    private int damage;
    public static float expansionProbability = 1f/64f;
    
    public GreenSlime() {
        sprite = new Sprite(texture, Tile.size, 4*Tile.size, Tile.size, Tile.size);
        
        passable = false;
        blocksVision = false;
        coversWholeTile = false;
        HP = 1000;
        hardness = 50;
        damage = 20;
    }
    
    public void expand() {
        Tile target = getExpansionTarget();
        if (target == null) return;
        
        if (target.hasObject() && !target.isPassable()) target.dealDamage(damage);
        else target.setObject(new GreenSlime());
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
