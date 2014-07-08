package wge3.game.entity.bombs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.Tile;

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