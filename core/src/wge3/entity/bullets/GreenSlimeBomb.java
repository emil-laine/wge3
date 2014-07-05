package wge3.entity.bullets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.entity.character.Bomb;
import wge3.entity.mapobjects.GreenSlime;
import wge3.world.Tile;

public class GreenSlimeBomb extends Bomb {
    
    public GreenSlimeBomb() {
        sprite = new Sprite(texture, 0, 4*Tile.size, Tile.size, Tile.size);
        
        range = 2;
        time = 2;
    }

    @Override
    public void explode() {
        float x = this.getX();
        float y = this.getY();
        int range = this.getRange();
        for (Tile currentTile : area.getTiles()) {
            if (currentTile.canBeSeenFrom(x, y, range)
            		&& !currentTile.hasObject()
            		&& !currentTile.hasCreature()) {
                currentTile.setObject(new GreenSlime());
            }
        }
        area.removeBomb(this);
    }
}
