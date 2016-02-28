/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.actors;

import wge3.model.StateFlag;
import wge3.model.Creature;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import static wge3.model.Team.PlayerTeam;
import wge3.engine.Statistics;

public final class Player extends Creature {
    
    private OrthographicCamera camera;
    private String name;
    public static Statistics statistics;
    
    public Player() {
        setSprite(4, 3);
        team = PlayerTeam;
        size = 14;
        HP.setMax(100);
        strength = 10;
        defense = 5;
        
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
