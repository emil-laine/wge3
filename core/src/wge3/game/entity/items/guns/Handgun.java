
package wge3.game.entity.items.guns;

import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;
import java.awt.geom.Line2D;
import static java.lang.Math.max;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import wge3.game.engine.constants.Statistic;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.items.Gun;
import static wge3.game.engine.gamestates.PlayState.mStream;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Player;

public final class Handgun extends Gun {
    
    public Handgun() {
        setSprite(1, 2);
        name = "handgun";
        range = 12;
        damage = 20;
    }
    
    @Override
    public void use(Creature user) {
        mStream.addMessage("BANG");
        user.getInventory().removeItem(this);
        
        float angle = user.getDirection();
        float dx = cos(angle);
        float dy = sin(angle);
        int range = getRange();
        int lofWidth = getLineOfFireWidth();
        // Starting point of the bullet:
        float originX = user.getX() + dx * (Tile.size/2);
        float originY = user.getY() + dy * (Tile.size/2);
        // Farthest point of the bullet:
        float targetX = originX + dx * range * Tile.size;
        float targetY = originY + dy * range * Tile.size;
        
        Line2D lineOfFire = new Line2D.Float(originX, originY, targetX, targetY);
        
        List<Creature> targets = new LinkedList<Creature>(user.getArea().getCreatures());

        // Remove impossible targets:
        targets.remove(user);
        for (Iterator<Creature> it = targets.iterator(); it.hasNext();) {
            Creature creature = it.next();
            if (lineOfFire.ptSegDist(creature.getX(), creature.getY()) > lofWidth) {
                it.remove();
            }
        }
        if (targets.isEmpty()) {
            return;
        }
        
        // Calculate the target closest to the starting point of the bullet:
        Creature target = targets.get(0);
        float targetDistance = target.getDistanceTo(originX, originY);
        
        for (int i = 1; i < targets.size(); i++) {
            Creature next = targets.get(i);
            float nextDistance = next.getDistanceTo(originX, originY);
            if (nextDistance < targetDistance) {
                target = next;
                targetDistance = target.getDistanceTo(originX, originY);
            }
        }
        
        // Always deals the same amount of damage, no matter what ptSegDist was.
        // This should be changed: lower ptSegDist -> greater damage.
        // Remember to change statistics counting if you do this ^!!
        target.dealDamage(getDamage());
        if (user.isPlayer()) {
            user.getStatistics().addStatToPlayer((Player) user, Statistic.GUNSHOTSFIRED, 1);
            user.getStatistics().addStatToPlayer((Player) user, Statistic.DAMAGEDEALT, max(getDamage() - target.getDefense(), 1));
        }
        
    }
}
