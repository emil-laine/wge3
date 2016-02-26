/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class GameStateManager {
    
    public static int WIDTH = Gdx.graphics.getWidth();
    public static int HEIGHT = Gdx.graphics.getHeight();
    
    private GameState currentState;
    private SpriteBatch batch;
    
    private String nextMap;
    
    public GameStateManager() {
        batch = new SpriteBatch();
    }
    
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        currentState = newState;
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
    
    public void draw() {
        currentState.draw(batch);
    }
}
