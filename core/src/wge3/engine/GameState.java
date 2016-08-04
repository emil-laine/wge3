/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import wge3.engine.util.Drawable;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.gui.GraphicsContext;

public abstract class GameState implements Drawable {
    
    private static GameStateManager stateManager;
    private static InputHandler input;
    private static GraphicsContext graphics;
    
    protected static GameStateManager getStateManager() {
        return stateManager;
    }
    
    protected static InputHandler getInputHandler() {
        return input;
    }
    
    protected static GraphicsContext getGraphicsContext() {
        return graphics;
    }
    
    static void setStateManager(GameStateManager gsm) {
        GameState.stateManager = gsm;
    }
    
    static void setGraphicsContext(GraphicsContext graphics) {
        GameState.graphics = graphics;
    }
    
    static void setInputHandler(InputHandler input) {
        GameState.input = input;
    }
    
    public void enter() {}
    public abstract void update(float delta);
    @Override
    public abstract void draw(Batch batch);
    public abstract void handleInput();
    public abstract void dispose();
}
