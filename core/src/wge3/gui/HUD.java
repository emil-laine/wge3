/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.gui;

import wge3.engine.util.Drawable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.engine.util.Drawable;
import static java.lang.Math.min;
import static wge3.engine.GameStateManager.HEIGHT;
import static wge3.engine.GameStateManager.WIDTH;
import wge3.model.actors.Player;

public final class HUD implements Drawable {
    
    private OrthographicCamera camera;
    private Texture texture;
    private ShapeRenderer sr;
    private BitmapFont font;
    private Player player;
    private int barThickness = 10;
    private int barMaxLength = 200;
    private int healthBarX = 1055;
    private int healthBarY = 653;
    private int energyBarX = healthBarX;
    private int energyBarY = healthBarY - 20;
    
    public HUD(Player player) {
        this.player = player;
        sr = new ShapeRenderer();
        font = new BitmapFont();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.translate(WIDTH/2, HEIGHT/2);
        camera.update();
        texture = new Texture(Gdx.files.internal("graphics/frame.png"));
    }
    
    @Override
    public void draw(Batch batch) {
        Gdx.graphics.getGL20().glViewport(
                0,
                0,
                WIDTH,
                HEIGHT);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(texture, 0, 0);
        
        // Text labels:
        font.draw(batch, "HP:", 1000, 660);
        font.draw(batch, "Energy:", 1000, 640);
        
        // Health and energy bars:
        batch.end();
        float health = player.getHP().asFraction();
        float healthBarLength = health * barMaxLength;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(
                min(-2*health+2, 1),
                min( 2*health,   1),
                0,
                1);
        sr.rectLine(
                healthBarX,
                healthBarY,
                healthBarX + healthBarLength,
                healthBarY,
                barThickness);
        
        float energy = player.getEnergy().asFraction();
        float energyBarLength = energy * barMaxLength;
        sr.setColor(
                min(-2*energy+2, 1),
                min( 2*energy,   1),
                0,
                1);
        sr.rectLine(
                energyBarX,
                energyBarY,
                energyBarX + energyBarLength,
                energyBarY,
                barThickness);
        sr.end();
        batch.begin();
    }
}
