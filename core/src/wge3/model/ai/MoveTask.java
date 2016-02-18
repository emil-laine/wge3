/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import java.util.ArrayList;
import java.util.List;
import wge3.model.Creature;
import static wge3.model.ai.PathFinder.findPath;
import wge3.model.Tile;
import static wge3.engine.util.Math.angle;
import static wge3.engine.util.Math.isInCenterOfATile;

public final class MoveTask extends AITask {
    
    private Creature executor;
    private TurnTask turnTask;
    private List<Tile> path;
    private int position;
    
    public MoveTask(Creature executor, Tile dest) {
        this.executor = executor;
        
        if (dest.isAnOKMoveDestinationFor(executor)) {
            path = new ArrayList(1);
            path.add(dest);
        } else {
            path = findPath(executor.getTileUnder(), dest);
            if (path == null) return;
        }
        
        position = 0;
        float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
        turnTask = new TurnTask(executor, angle);
    }
    
    @Override
    public boolean isFinished() {
        return path == null || (executor.getTileUnder().equals(getDestination()) && isInCenterOfATile(executor));
    }
    
    @Override
    public void execute() {
        if (!turnTask.isFinished()) {
            turnTask.execute();
            return;
        }
        
        if (executor.getTileUnder().equals(path.get(position)) && isInCenterOfATile(executor)) {
            position++;
            float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
            turnTask = new TurnTask(executor, angle);
        } else {
            executor.goForward();
        }
    }
    
    public Tile getDestination() {
        return path == null ? null : path.get(path.size()-1);
    }
    
    public void setDestination(Tile dest) {
        if (path != null) path.set(path.size()-1, dest);
    }
}
