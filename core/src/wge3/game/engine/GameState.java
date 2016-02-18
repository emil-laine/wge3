/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.game.engine.gui.Drawable;

public abstract class GameState implements Drawable {
    
    protected GameStateManager gsm;
    protected InputHandler input;
    
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        input = new InputHandler();
        Gdx.input.setInputProcessor(input);
    }
    
    public abstract void init();
    public abstract void update(float delta);
    @Override
    public abstract void draw(Batch batch);
    public abstract void handleInput();
    public abstract void dispose();
}
