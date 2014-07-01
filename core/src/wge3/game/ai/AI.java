package wge3.game.ai;

import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.world.Tile;

public final class AI {

    protected NonPlayer NPC; // the controlled creature
    protected AITask currentTask;

    public AI(NonPlayer creature) {
        this.NPC = creature;
        currentTask = new WaitTask(0); // dummy task
    }
    
    public void update() {
        // Check if creature can see an enemy:
        if (currentTask.getClass() != AttackTask.class) {
            for (Creature dude : creature.getArea().getCreatures()) {
                if (dude.getTeam() != creature.getTeam() && dude.canBeSeenBy(creature)) {
                    Tile tile = getTileBeforeObstacle(dude);
                    if (tile == null) return;
                    
                    if (tile != creature.getTile()) {
                        currentTask = new MoveTask(creature, tile);
                        return;
                    } else {
                        for (int i = 0; i < 31; i++) {
                            for (int j = 1; j > -2; j -= 2) {
                                Tile newtile = getAlternativeDestinationTile(j, i);
                                currentTask = new MoveTask(creature, newtile);
                            }
                        }
                    }
                }
                
                if (dude.getTile().isAnOKMoveDestinationFor(creature) && dude.getTeam() != creature.getTeam()) {
                    currentTask = new AttackTask(creature, dude);
                    return;
                }
            }
        }
        
        if (!currentTask.isFinished()) {
            currentTask.execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            currentTask = new MoveTask(creature, creature.getNewMovementDestination());
            currentTask = new MoveTask(NPC, NPC.getNewMovementDestination());
        } else {
            currentTask = new WaitTask(random(3000));
        }
    }

    public boolean isAttacking() {
        return currentTask.getClass() == AttackTask.class;
    }
    
    public Tile getTileBeforeObstacle(Creature enemy) {
        float startX = creature.getX();
        float startY = creature.getY();
        float dx = enemy.getX() - startX;
        float dy = enemy.getY() - startY;
        float distance = (float) Math.sqrt(dx*dx + dy*dy);
        distance /= Tile.size;
        
        for (int i = 1; i <= distance; i++) {
            if (creature.getArea().hasLocation(startX + i*(dx*Tile.size), startY + i*(dy*Tile.size))) {
                Tile currentTile = creature.getArea().getTileAt(startX + i*(dx*Tile.size), startY + i*(dy*Tile.size));
                if (!currentTile.isPassable() || currentTile.drainsHP()) {
                    return creature.getArea().getTileAt(startX + (i-1)*(dx*Tile.size), startY + (i-1)*(dy*Tile.size));
                }
            }
        }
        return null;
    }

    public Tile getAlternativeDestinationTile(int posneg, int i) {
        float angle = creature.getDirection();
        angle += PI/2*posneg;
        float dx = MathUtils.cos(angle);
        float dy = MathUtils.sin(angle);
        
        return NPC.getArea().getTileAt(i*dx*Tile.size, i*dy*Tile.size);
    }
}