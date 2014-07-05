package wge3.entity.mapobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.random;
import java.util.List;
import wge3.entity.terrainelements.MapObject;
import wge3.world.Tile;

public class GreenSlime extends MapObject {

    private int damage;
    
    public GreenSlime() {
        sprite = new Sprite(texture, Tile.size, 4*Tile.size, Tile.size, Tile.size);
        
        passable = false;
        blocksVision = false;
        HP = 1000;
        hardness = 50;
        damage = 20;
    }
    
    public void expand() {
        List<Tile> tiles = tile.getNearbyTiles();
        Tile target = tiles.get(random(tiles.size()-1));
        if (!target.hasObject()  && !tile.hasCreature()) target.setObject(new GreenSlime());
        if (target.hasSlime()) return;
        else target.dealDamage(damage);
    }
}
