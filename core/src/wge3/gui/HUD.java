/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static java.lang.Math.min;
import wge3.engine.util.Drawable;
import wge3.model.Player;

public final class HUD implements Drawable {
    
    private OrthographicCamera camera;
    private GraphicsContext graphics;
    private Player player;
    private int barThickness = 10;
    private int barMaxLength = 200;
    private int healthBarX = 1055;
    private int healthBarY = 653;
    
    public HUD(Player player, GraphicsContext graphics) {
        this.player = player;
        this.graphics = graphics;
        camera = new OrthographicCamera(graphics.getLogicalWidth(),
                                        graphics.getLogicalHeight());
        camera.translate(graphics.getLogicalWidth()/2,
                         graphics.getLogicalHeight()/2);
        camera.update();
    }
    
    @Override
    public void draw(Batch batch) {
        Gdx.graphics.getGL20().glViewport(
                0,
                0,
                graphics.getLogicalWidth(),
                graphics.getLogicalHeight());
        batch.setProjectionMatrix(camera.combined);
        
        // Text labels:
        graphics.getFont().draw(batch, "HP:", 1000, 660);
        
        // Health bar:
        batch.end();
        ShapeRenderer sr = graphics.getShapeRenderer();
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
        sr.end();
        batch.begin();
    }
}
