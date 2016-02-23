/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.PI;
import java.util.ArrayList;
import java.util.List;
import wge3.engine.util.Debug;
import wge3.model.Creature;
import static wge3.model.ai.PathFinder.findPath;
import wge3.model.Tile;
import static wge3.engine.util.Math.angle;
import static wge3.engine.util.Math.getDiff;
import static wge3.engine.util.Math.isInCenterOfATile;

public final class MoveTask extends AITask {
    
    private Creature executor;
    private TurnTask turnTask;
    private List<Tile> path;
    private int position;
    
    public MoveTask(Creature executor, Tile dest) {
        this.executor = executor;
        setDestination(dest);
    }
    
    public void setDestination(Tile dest) {
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
        }
        
        float angle = angle(executor.getX(), executor.getY(), path.get(position).getMiddleX(), path.get(position).getMiddleY());
        float diff = getDiff(executor.getDirection(), angle);
        
        if (Math.abs(diff) > PI/48) {
            turnTask = new TurnTask(executor, angle);
        } else {
            executor.goForward();
        }
    }
    
    public Tile getDestination() {
        return path == null ? null : path.get(path.size()-1);
    }
    
    Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
    float circleSize = MathUtils.random();
    
    // For debugging
    void draw(Batch batch) {
        ShapeRenderer sr = Debug.getShapeRenderer();
        
        batch.end();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(color);
        sr.begin(ShapeType.Line);
        if (path.size() == 1) {
            sr.circle(path.get(0).getMiddleX(), path.get(0).getMiddleY(), Tile.size/2 * circleSize);
        } else {
            for (int i = 1; i < path.size(); i++) {
                Tile t1 = path.get(i-1);
                Tile t2 = path.get(i);
                sr.line(t1.getMiddleX(), t1.getMiddleY(),
                        t2.getMiddleX(), t2.getMiddleY());
            }
        }
        sr.circle(executor.getX(), executor.getY(), Tile.size/2 * circleSize);
        sr.end();
        batch.begin();
    }
}
