/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import wge3.gui.GraphicsContext;

public final class WGE3 extends Game {
    
    private GameStateManager stateManager;
    private GraphicsContext graphics;
    
    @Override
    public void create () {
        stateManager = new GameStateManager();
        graphics = new GraphicsContext();
        GameState.setStateManager(stateManager);
        GameState.setGraphicsContext(graphics);
        GameState.setInputHandler(new InputHandler());
        stateManager.pushState(new MenuState());
        
        Audio.playMusic("soundtrack.mp3");
    }
    
    @Override
    public void render () {
        stateManager.update(Gdx.graphics.getDeltaTime());
        
        graphics.beginDraw();
        stateManager.draw(graphics.getBatch());
        graphics.endDraw();
    }
    
    @Override
    public void dispose() {
        Audio.dispose();
        graphics.dispose();
        stateManager.dispose();
    }
}
