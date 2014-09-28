package wge3.game.engine.ai;

import wge3.game.engine.ai.tasks.WaitTask;
import wge3.game.engine.ai.tasks.AITask;
import wge3.game.engine.ai.tasks.MeleeAttackTask;
import wge3.game.engine.ai.tasks.MoveTask;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.sin;
import static java.lang.Integer.signum;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.Tile;

public class AI {

    protected NonPlayer NPC; // the controlled creature
    protected AITask currentTask;

    public AI(NonPlayer creature) {
        this.NPC = creature;
        currentTask = new WaitTask(0); // dummy task
    }
    
    public void update() {
        if (!isAttacking()) {
            checkForEnemies();
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(NPC, NPC.getNewMovementDestination());
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }

    public void checkForEnemies() {
        for (Creature enemy : NPC.getEnemiesWithinFOV()) {
            // If dude is located in an OK move destination, attack:
            if (enemy.getTile().isAnOKMoveDestinationFor(NPC)) {
                currentTask = new MeleeAttackTask(NPC, enemy);
                return;
            }
            
            // Else, dude is in a not-OK move destination,
            // so try to find nearest OK move destination:
            
            /* getNearestOKMoveDestination makes NPCs freeze atm */
            /* It should be rewritten. */
            
            //Tile nearestOKTile = getNearestOKMoveDestination(dude);
            //if (nearestOKTile != null) {
            //    currentTask = new MoveTask(NPC, nearestOKTile);
            //}
            
            // If nearestOKTile was null, ignore enemy.
        }
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
                if (!currentTile.isPassable() || currentTile.drainsHP()) {
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
}