/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import wge3.model.Creature;
import wge3.model.NonPlayer;
import wge3.model.Tile;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;

/**
 *
 * @author chang
 */

// The thief is a coward: he only acts when buddies are around. He closes in on his enemy FAST and takes an object he is carrying.
// He then runs away. If the player can catch the thief and kill him, he can recover his items. The thief will do anything he can to avoid capture.
public class ThiefAI extends AI {
    
    private int cowardLevel; // How many friendlies should be nearby for the thief to become active
    private boolean itemStolen;
    private boolean fleeing;
    
    public ThiefAI(NonPlayer creature) {
        super(creature);
        cowardLevel = 4;
        itemStolen = false;
        fleeing = false;
    }
    
    public void setCowardLevel(int newLevel) {
        if (newLevel > 0) {
            cowardLevel = newLevel;
        }
    }
    
    @Override
    public void update() {
        if (!isStealing()) {
            checkForEnemies();
        }
        
        if (!getCurrentTask().isFinished()) {
            getCurrentTask().execute();
            return;
        }
        
        if (randomBoolean(2/3f)) {
            setCurrentTask(new MoveTask(getNPC(), getNewMovementDestination(getNPC())));
        } else {
            setCurrentTask(new WaitTask(random(3000)));
        }
    }
    
    @Override
    public void checkForEnemies() {
        for (Creature enemy : getNPC().getEnemiesWithinFOV()) {
            if (!enoughFriendliesNearby() || !getNPC().getInventory().getItems().isEmpty() || enemy.getInventory().getItems().isEmpty()) {
                if (fleeing) return;
                fleeing = true;
                setCurrentTask(new MoveTask(getNPC(), whereToRun(enemy)));
                return;
            }
            fleeing = false;
            setCurrentTask(new StealTask(getNPC(), enemy));
            break; // TODO: Should steal from the nearest enemy instead of
                   // always picking the first one in the returned list.
        }
    }
    
    public boolean enoughFriendliesNearby() {
        return getNPC().getFriendliesWithinFOV().size() >= cowardLevel;
    }
    
    public boolean isStealing() {
        return getCurrentTask().getClass() == StealTask.class;
    }
    
    // Plots a place to run away from enemy
    private Tile whereToRun(Creature enemy) {
        return enemy.getArea().getRandomTileWithoutObject();
    }
}
