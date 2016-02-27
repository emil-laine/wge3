/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import java.util.Stack;

public final class GameStateManager implements Disposable {
    
    private Stack<GameState> states;
    
    public GameStateManager() {
        states = new Stack();
    }
    
    public void pushState(GameState newState) {
        states.push(newState);
        newState.enter();
    }
    
    public void popState() {
        states.pop().dispose();
        
        if (!states.empty()) {
            states.peek().enter();
        }
    }
    
    @Override
    public void dispose() {
        while (!states.empty()) {
            popState();
        }
    }
    
    public void update(float delta) {
        states.peek().update(delta);
    }
    
    public void draw(Batch batch) {
        states.peek().draw(batch);
    }
    
}
