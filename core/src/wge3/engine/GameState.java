/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import wge3.engine.util.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.gui.GraphicsContext;

public abstract class GameState implements Drawable {
    
    protected GameStateManager gsm;
    protected InputHandler input;
    protected GraphicsContext graphics;
    
    public GameState(GameStateManager gsm, GraphicsContext graphics) {
        this.gsm = gsm;
        input = new InputHandler();
        Gdx.input.setInputProcessor(input);
        this.graphics = graphics;
    }
    
    public abstract void init();
    public abstract void update(float delta);
    @Override
    public abstract void draw(Batch batch);
    public abstract void handleInput();
    public abstract void dispose();
}
