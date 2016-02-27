/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public final class GameStateManager implements Disposable {
    
    private GameState currentState;
    private String nextMap;
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        currentState = newState;
    }
    
    @Override
    public void dispose() {
        if (currentState != null) {
            currentState.dispose();
        }
    }
    
    public String getNextMap() {
        return nextMap;
    }
    
    public void setNextMap(String nextMap) {
        this.nextMap = nextMap;
    }
    
    public void update(float delta) {
        currentState.update(delta);
    }
    
    public void draw(Batch batch) {
        currentState.draw(batch);
    }
    
}
