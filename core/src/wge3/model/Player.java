/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.engine.Statistics;
import static wge3.model.Team.PlayerTeam;

public final class Player extends Creature {
    
    private OrthographicCamera camera;
    private String name;
    public static Statistics statistics;
    
    public Player() {
        super("player");
        team = PlayerTeam;
        getStateFlags().add(StateFlag.PICKS_UP_ITEMS);
    }
    
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
    
    @Override
    public void move(float dx, float dy) {
        float oldX = getX();
        float oldY = getY();
        super.move(dx, dy);
        camera.translate(getX() - oldX, getY() - oldY);
        camera.update();
    }
    
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    
    public String getName() {
        return name;
    }
    
    public void setStats(Statistics statistics) {
        Player.statistics = statistics;
    }
    
    public static Statistics getStats() {
        return statistics;
    }
}
