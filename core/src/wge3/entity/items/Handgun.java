
package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import wge3.entity.character.Creature;
import static wge3.game.gamestates.PlayState.mStream;
import wge3.world.Tile;

public final class Handgun extends Gun {
    
    public Handgun() {
        sprite = new Sprite(texture, Tile.size, 2*Tile.size, Tile.size, Tile.size);
        name = "handgun";
        setRange(31);
        setDamage(999);
    }
    
    @Override
    public void use(Creature user) {
        mStream.addMessage("BANG");
        user.getInventory().removeItem(this);
        
        float angle = user.getDirection();
        float dx = MathUtils.cos(angle);
        float dy = MathUtils.sin(angle);
        float startX = user.getX();
        float startY = user.getY();
        int range = getRange();
        
        for (int i = 0; i <= range; i++) {
            Tile currentTile = user.getArea().getTileAt(startX + i*(dx*Tile.size), startY + i*(dy*Tile.size));
            
            if (currentTile == null || currentTile.getCreatures().get(0).isPlayer()) {
                return;
            }
            
            if (currentTile.hasCreature()) {
                currentTile.getCreatures().get(0).dealDamage(getDamage());
                return;
            }
            else if (!currentTile.isPassable() && currentTile.blocksVision() ) {
                return;
            }
        }
    }
}
