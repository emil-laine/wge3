
package wge3.game.entity.tilelayers.mapobjects.items.guns;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import java.awt.geom.Line2D;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.tilelayers.mapobjects.items.Gun;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.Tile;

public final class Handgun extends Gun {
    
    public Handgun() {
        sprite = new Sprite(texture, Tile.size, 2*Tile.size, Tile.size, Tile.size);
        name = "handgun";
        range = 12;
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
        float originX = user.getX();
        float originY = user.getY();
        float targetX = originX + dx * range * Tile.size;
        float targetY = originY + dy * range * Tile.size;
        
        Line2D lineOfFire = new Line2D.Float(originX, originY, targetX, targetY);
        
        for (Creature creature : user.getArea().getCreatures()) {
            if (creature.equals(user)) continue;
            if (lineOfFire.ptSegDist(creature.getX(), creature.getY()) < getLineOfFireWidth()) {
                creature.dealDamage(getDamage());
            }
        }
    }
}
