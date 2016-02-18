/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public final class WGE3 extends Game {
    
    private GameStateManager gsm;
    
    @Override
    public void create () {
        gsm = new GameStateManager();
        gsm.setState(0);
        
        Gdx.gl.glClearColor(1/12f, 1/12f, 1/12f, 1);
    }
    
    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.draw();
    }
}
