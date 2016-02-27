/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class GraphicsContext implements Disposable {
    
    private int logicalWidth;
    private int logicalHeight;
    private SpriteBatch batch;
    private FrameBuffer frameBuffer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    
    public GraphicsContext() {
        logicalWidth = Gdx.graphics.getWidth();
        logicalHeight = Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        initFrameBuffer(logicalWidth, logicalHeight);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        Gdx.gl.glClearColor(1/12f, 1/12f, 1/12f, 1);
    }
    
    private void initFrameBuffer(int frameBufferWidth, int frameBufferHeight) {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }
        
        frameBuffer = new FrameBuffer(Pixmap.Format.RGB565,
                                      frameBufferWidth, frameBufferHeight,
                                      false);
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest,
                                                      Texture.TextureFilter.Nearest);
    }
    
    @Override
    public void dispose() {
        frameBuffer.dispose();
        batch.dispose();
    }
    
    public void beginDraw() {
        frameBuffer.begin();
        Gdx.gl.glViewport(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    public void endDraw() {
        frameBuffer.end();
        
        batch.begin();
        batch.draw(frameBuffer.getColorBufferTexture(),
                   0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                   0, 0, 1, 1);
        batch.end();
    }
    
    public int getLogicalWidth() {
        return logicalWidth;
    }
    
    public int getLogicalHeight() {
        return logicalHeight;
    }
    
    public BitmapFont getFont() {
        return font;
    }
    
    public Batch getBatch() {
        return batch;
    }
    
    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}
