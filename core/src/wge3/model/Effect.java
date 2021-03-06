/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import wge3.engine.Audio;
import static wge3.engine.PlayState.messageStream;
import wge3.engine.Statistic;
import wge3.engine.util.Config;
import static java.lang.Math.max;
import static wge3.engine.util.Math.getDistance;

public class Effect {
    
    private Creature user;
    private ItemInstance item;
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
    
    public void activate(Creature user, ItemInstance item) {
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
    
    @SuppressWarnings("unused") // used via reflection
    private void dropAndStartTimer() {
        item.setCanBePickedUp(false);
        user.drop(item);
        item.getComponents().stream().forEach(c -> c.use(user));
        
        Method method;
        String timedEffect = cfg.getString(supertype, "timedEffect");
        try {
            method = getClass().getDeclaredMethod(timedEffect);
        } catch (NoSuchMethodException ex) {
            throw new UnsupportedOperationException("Unknown timedEffect: '" + timedEffect + "'");
        }
        Runnable effect = () -> {
            try {
                method.invoke(this);
            } catch (IllegalAccessException |
                     IllegalArgumentException |
                     InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        };
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                effect.run();
            }
        }, cfg.getInt(supertype, "time"));
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void bombExplosion() {
        messageStream.addMessage("*EXPLOSION*");
        
        int range = cfg.getInt(supertype, "range");
        int damage = cfg.getInt(supertype, "damage");
        
        for (Tile currentTile : item.getArea().getTiles()) {
            if (currentTile.canBeSeenFrom(item.getX(), item.getY(), range)) {
                float distance = currentTile.getDistanceTo(item.getX(), item.getY()) / Tile.size;
                float intensity = 1f - Math.max(distance-1f, 0f) * (1f / range);
                // intensity = 1, when distance = [0,1].
                currentTile.dealDamage((int) (intensity*damage));
            }
        }
        item.getArea().removeEntity(item);
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void spawnGreenSlime() {
        int range = cfg.getInt(supertype, "range");
        
        for (Tile currentTile : item.getArea().getTiles()) {
            if (currentTile.canBeSeenFrom(item.getX(), item.getY(), range)
                    && !currentTile.hasObject()
                    && !currentTile.hasCreature()) {
                currentTile.setObject(new MapObject("greenSlime"));
            }
        }
        item.getArea().removeEntity(item);
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void shootProjectile() {
        int range = cfg.getInt(supertype, "range");
        int damage = cfg.getInt(supertype, "damage");
        int lofWidth = (int) (Tile.size * cfg.getFloat(supertype, "lineOfFireWidth"));
        
        Audio.playSound("defaultGun.wav");
        messageStream.addMessage("BANG");
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
        user.incrementStat(Statistic.GUNSHOTS_FIRED, 1);
        user.incrementStat(Statistic.DAMAGE_DEALT, max(damage - target.getDefense(), 1));
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void healSelf() {
        int healAmount = cfg.getInt(supertype, "healAmount");
        user.addHP(healAmount);
        user.removeItem(item);
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void invisibility() {
        int duration = cfg.getInt(supertype, "duration");
        
        messageStream.addMessage("*glug*");
        user.removeItem(item);
        user.setInvisibility(true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                user.setInvisibility(false);
            }
        }, duration);
    }
    
    @SuppressWarnings("unused") // used via reflection
    private void boostSpeed() {
        int duration = cfg.getInt(supertype, "duration");
        float boostMultiplier = cfg.getFloat(supertype, "boostMultiplier");
        
        messageStream.addMessage("*glug*");
        user.removeItem(item);
        user.setCurrentSpeed((int) (user.getCurrentSpeed() * boostMultiplier));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                user.setCurrentSpeed(user.getDefaultSpeed());
            }
        }, duration);
    }
}
