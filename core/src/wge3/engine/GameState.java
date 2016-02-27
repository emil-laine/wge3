/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import wge3.engine.util.Drawable;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.gui.GraphicsContext;

public abstract class GameState implements Drawable {
    
    protected static GameStateManager gsm;
    protected static InputHandler input;
    protected static GraphicsContext graphics;
    
    static void setStateManager(GameStateManager gsm) {
        GameState.gsm = gsm;
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
