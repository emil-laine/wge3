
package wge3.entity.items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import java.awt.geom.Line2D;
import wge3.entity.character.Creature;
import static wge3.game.gamestates.PlayState.mStream;
import wge3.world.Tile;

public final class Handgun extends Gun {
    
    public Handgun() {
        sprite = new Sprite(texture, Tile.size, 2*Tile.size, Tile.size, Tile.size);
        name = "handgun";
        range = 31;
        damage = 99;
    }
    
    @Override
    public void use(Creature user) {
        mStream.addMessage("BANG");
        user.getInventory().removeItem(this);
        
        float angle = user.getDirection();
        float dx = cos(angle);
        float dy = sin(angle);
        int range = getRange();
        float targetX = dx * range;
        float targetY = dy * range;
        
        Line2D lineOfFire = new Line2D.Float(user.getX(), user.getY(), targetX, targetY);
        
        for (Creature creature : user.getArea().getCreatures()) {
            if (creature.equals(user)) continue;
            if (lineOfFire.ptSegDist(creature.getX(), creature.getY()) < getLineOfFireWidth()) {
                creature.dealDamage(getDamage());
            }
        }
    }
}
