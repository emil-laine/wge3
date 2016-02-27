/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import wge3.gui.GraphicsContext;

public final class WGE3 extends Game {
    
    private GameStateManager gsm;
    private GraphicsContext graphics;
    
    @Override
    public void create () {
        gsm = new GameStateManager();
        graphics = new GraphicsContext();
        gsm.setState(new MenuState(gsm, graphics));
        
        Audio.playMusic("soundtrack.mp3");
    }
    
    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        
        graphics.beginDraw();
        gsm.draw(graphics.getBatch());
        graphics.endDraw();
    }
    
    @Override
    public void dispose() {
        Audio.dispose();
        graphics.dispose();
        gsm.dispose();
    }
}
