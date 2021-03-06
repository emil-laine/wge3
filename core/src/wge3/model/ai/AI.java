/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.sin;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import static java.lang.Math.signum;
import java.util.ArrayList;
import java.util.List;
import wge3.engine.util.Debug;
import wge3.model.Creature;
import wge3.model.NonPlayer;
import wge3.model.Tile;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static java.lang.Math.signum;

public class AI {
    
    private NonPlayer NPC; // the controlled creature
    private AITask currentTask;
    private long lastTimeOfEnemyCheck;
    
    public AI(NonPlayer creature) {
        this.NPC = creature;
        currentTask = new WaitTask(0); // dummy task
        lastTimeOfEnemyCheck = millis();
    }
    
    public void update() {
        if (!isAttacking() && canCheckForEnemies()) {
            checkForEnemies();
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(NPC, getNewMovementDestination(NPC));
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }
    
    public void checkForEnemies() {
        lastTimeOfEnemyCheck = millis();
        
        NPC.getEnemiesWithinFOV()
                .parallelStream()
                .filter(x -> NPC.canMoveTo(x.getTileUnder()))
                .findFirst()
                .ifPresent(x -> currentTask = new MeleeAttackTask(NPC, x));
        
        // Does the same as the code below, except multithreaded
        
//        for (Creature enemy : NPC.getEnemiesWithinFOV()) {
//            // If dude is located in an OK move destination, attack:
//            if (NPC.canMoveTo(enemy.getTile())) {
//                currentTask = new MeleeAttackTask(NPC, enemy);
//                return;
//            }
//            
//            // Else, dude is in a not-OK move destination,
//            // so try to find nearest OK move destination:
//            
//            /* getNearestOKMoveDestination makes NPCs freeze atm */
//            /* It should be rewritten. */
//            
//            //Tile nearestOKTile = getNearestOKMoveDestination(dude);
//            //if (nearestOKTile != null) {
//            //    currentTask = new MoveTask(NPC, nearestOKTile);
//            //}
//            
//            // If nearestOKTile was null, ignore enemy.
//        }
    }
    
    public boolean isAttacking() {
        return currentTask.getClass() == MeleeAttackTask.class;
    }
    
    public Tile getTileBeforeObstacle(Creature enemy) {
        float startX = NPC.getX();
        float startY = NPC.getY();
        float dx = enemy.getX() - startX;
        float dy = enemy.getY() - startY;
        float distance = (float) (Math.sqrt(dx*dx + dy*dy) / Tile.size);
        
        for (int i = 1; i <= distance; i++) {
            if (NPC.getArea().hasLocation(startX + i*(dx*Tile.size), startY + i*(dy*Tile.size))) {
                Tile currentTile = NPC.getArea().getTileAt(startX + i*(dx*Tile.size), startY + i*(dy*Tile.size));
                if (!currentTile.isPassable() || currentTile.getHPDrainAmount() > 0) {
                    return NPC.getArea().getTileAt(startX + (i-1)*(dx*Tile.size), startY + (i-1)*(dy*Tile.size));
                }
            }
        }
        return null;
    }
    
    public Tile getAlternativeDestinationTile(int i) {
        float angle = NPC.getDirection();
        angle += PI/2 * signum(i);
        float dx = cos(angle);
        float dy = sin(angle);
        
        float tileX = i*dx*Tile.size;
        float tileY = i*dy*Tile.size;
        
        if (NPC.getArea().hasLocation(tileX, tileY)) {
            return NPC.getArea().getTileAt(tileX, tileY);
        }
        
        return null;
    }
    
    public Tile getNearestOKMoveDestination(Creature dude) {
        for (int i = 0; i < 31; i++) {
            for (int j = 1; j > -2; j -= 2) {
                Tile newtile = getAlternativeDestinationTile(j*i);
                
                if (newtile == null) {
                    continue;
                }
                
                if (newtile.isAnOKMoveDestinationFor(NPC)) {
                    return newtile;
                }
            }
        }
        
        return null;
    }
    
    private boolean canCheckForEnemies() {
        return millis() - lastTimeOfEnemyCheck > 500;
    }
    
    public static List<Tile> getPossibleMovementDestinations(NonPlayer NPC) {
        List<Tile> tiles = new ArrayList();
        NPC.getArea().getTiles()
                .stream()
                .filter((tile) -> (tile.canBeSeenBy(NPC) && tile.isAnOKMoveDestinationFor(NPC)))
                .forEach((tile) -> tiles.add(tile));
        return tiles;
    }
    
    public static Tile getNewMovementDestination(NonPlayer NPC) {
        // Ugly method for testing only!
        // NPCs should decide their next movement destinations
        // more intelligently rather than just by random.
        
        // Returns a random tile from all the tiles that are
        // ok move destinations and can be seen by creature.
        List<Tile> tiles = getPossibleMovementDestinations(NPC);
        return tiles.get(random(tiles.size() - 1));
    }
    
    // For debugging
    public void draw(Batch batch) {
        if (Debug.moveTaskDebug) {
            if (currentTask instanceof MoveTask) {
                ((MoveTask) currentTask).draw(batch);
            } else if (currentTask instanceof MeleeAttackTask) {
                ((MeleeAttackTask) currentTask).draw(batch);
            }
        }
    }
    
    protected NonPlayer getNPC() {
        return NPC;
    }
    
    protected AITask getCurrentTask() {
        return currentTask;
    }
    
    protected void setCurrentTask(AITask newTask) {
        currentTask = newTask;
    }
}