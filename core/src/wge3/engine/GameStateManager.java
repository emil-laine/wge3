/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

public final class GameStateManager implements Disposable {
    
    public static int WIDTH = Gdx.graphics.getWidth();
    public static int HEIGHT = Gdx.graphics.getHeight();
    
    private GameState currentState;
    private SpriteBatch batch;
    private FrameBuffer frameBuffer;
    
    private String nextMap;
    
    public GameStateManager() {
        batch = new SpriteBatch();
        initFrameBuffer(WIDTH, HEIGHT);
    }
    
    private void initFrameBuffer(int frameBufferWidth, int frameBufferHeight) {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }
        
        frameBuffer = new FrameBuffer(Pixmap.Format.RGB565,
                                      frameBufferWidth, frameBufferHeight,
                                      false);
        frameBuffer.getColorBufferTexture().setFilter(TextureFilter.Nearest,
                                                      TextureFilter.Nearest);
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
        frameBuffer.begin();
        Gdx.gl.glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentState.draw(batch);
        frameBuffer.end();
        
        batch.begin();
        batch.draw(frameBuffer.getColorBufferTexture(),
                   0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                   0, 0, 1, 1);
        batch.end();
    }
    
    @Override
    public void dispose() {
        frameBuffer.dispose();
        batch.dispose();
    }
}
