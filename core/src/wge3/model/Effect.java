/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import wge3.engine.Audio;
import static wge3.engine.PlayState.mStream;
import wge3.engine.Statistic;
import wge3.engine.util.Config;
import static java.lang.Math.max;
import static wge3.engine.util.Math.getDistance;

public class Effect {
    
    private Creature user;
    private Item item;
    private Method method;
    private Config cfg;
    private String supertype;
    
    public Effect(String type, Config cfg, String supertype) {
        try {
            method = getClass().getDeclaredMethod(type);
        } catch (NoSuchMethodException ex) {
            throw new UnsupportedOperationException("Unknown effect type: '" + type + "'");
        }
        
        this.cfg = cfg;
        this.supertype = supertype;
    }
    
    public void activate(Creature user, Item item) {
        this.user = user;
        this.item = item;
        
        try {
            method.invoke(this);
        } catch (IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void dropAndStartTimer() {
        item.setCanBePickedUp(false);
        user.drop(item);
        item.getComponents().stream().forEach(c -> c.use(user));
    }
    
    private void bombExplosion() {
        mStream.addMessage("*EXPLOSION*");
        
        int range = cfg.getInt(supertype, "range");
        int damage = cfg.getInt(supertype, "damage");
        
        for (Tile currentTile : user.getArea().getTiles()) {
            if (currentTile.canBeSeenFrom(user.getX(), user.getY(), range)) {
                float distance = currentTile.getDistanceTo(user.getX(), user.getY()) / Tile.size;
                float intensity = 1f - Math.max(distance-1f, 0f) * (1f / range);
                // intensity = 1, when distance = [0,1].
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
        user.getArea().removeEntity(item);
    }
    
    private void spawnGreenSlime() {
        int range = cfg.getInt(supertype, "range");
        
        for (Tile currentTile : user.getArea().getTiles()) {
            if (currentTile.canBeSeenFrom(user.getX(), user.getY(), range)
                    && !currentTile.hasObject()
                    && !currentTile.hasCreature()) {
                currentTile.setObject(new MapObject("greenSlime"));
            }
        }
        user.getArea().removeEntity(item);
    }
    
    private void shootProjectile() {
        int range = cfg.getInt(supertype, "range");
        int damage = cfg.getInt(supertype, "damage");
        int lofWidth = (int) (Tile.size * cfg.getFloat(supertype, "lineOfFireWidth"));
        
        Audio.playSound("defaultGun.wav");
        mStream.addMessage("BANG");
        user.removeItem(item);
        
        float angle = user.getDirection();
        float dx = MathUtils.cos(angle);
        float dy = MathUtils.sin(angle);
        // Starting point of the bullet:
        float originX = user.getX() + dx * (Tile.size/2);
        float originY = user.getY() + dy * (Tile.size/2);
        // Farthest point of the bullet:
        float targetX = originX + dx * range * Tile.size;
        float targetY = originY + dy * range * Tile.size;
        
        Line2D lineOfFire = new Line2D.Float(originX, originY, targetX, targetY);
        
        List<Creature> targets = new LinkedList(user.getArea().getCreatures());
        
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
        float targetDistance = getDistance(target, originX, originY);
        
        for (int i = 1; i < targets.size(); i++) {
            Creature next = targets.get(i);
            float nextDistance = getDistance(next, originX, originY);
            if (nextDistance < targetDistance) {
                target = next;
                targetDistance = getDistance(target, originX, originY);
            }
        }
        
        // Always deals the same amount of damage, no matter what ptSegDist was.
        // This should be changed: lower ptSegDist -> greater damage.
        // Remember to change statistics counting if you do this ^!!
        target.dealDamage(damage);
        if (user.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) user, Statistic.GUNSHOTSFIRED, 1);
            Player.statistics.addStatToPlayer((Player) user, Statistic.DAMAGEDEALT, max(damage - target.getDefense(), 1));
        }
    }
    
    private void healSelf() {
        int healAmount = cfg.getInt(supertype, "healAmount");
        
        user.addHP(healAmount);
        if (user.getCurrentHP() - healAmount >= 0) {
            Player.statistics.addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, healAmount);
        }
        else {
            Player.statistics.addStatToPlayer((Player) user, Statistic.HEALTHREGAINED, (user.getMaxHP() - user.getCurrentHP()));
        }
        
        user.removeItem(item);
    }
    
    private void invisibility() {
        int duration = cfg.getInt(supertype, "duration");
        
        mStream.addMessage("*glug*");
        user.removeItem(item);
        user.setInvisibility(true);
        new Timer().scheduleTask(new Task() {
            @Override
            public void run() {
                user.setInvisibility(false);
            }
        }, duration);
    }
    
    private void boostSpeed() {
        int duration = cfg.getInt(supertype, "duration");
        float boostMultiplier = cfg.getFloat(supertype, "boostMultiplier");
        
        mStream.addMessage("*glug*");
        user.removeItem(item);
        user.setCurrentSpeed((int) (user.getCurrentSpeed() * boostMultiplier));
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                user.setCurrentSpeed(user.getDefaultSpeed());
            }
        }, duration);
    }
}
