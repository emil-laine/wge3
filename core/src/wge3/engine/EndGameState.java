/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

// Remember to implement statistics

public final class EndGameState extends GameState {
    
    private boolean result;
    private Statistics statistics;
    
    public EndGameState(boolean result, Statistics statistics) {
        this.result = result;
        this.statistics = statistics;
    }
    
    @Override
    public void enter() {
        Gdx.input.setInputProcessor(getInputHandler());
    }
    
    @Override
    public void update(float delta) {
        handleInput();
    }
    
    @Override
    public void draw(Batch batch) {
        batch.begin();
        int maxX = getGraphicsContext().getLogicalWidth();
        int maxY = getGraphicsContext().getLogicalHeight();
        BitmapFont font = getGraphicsContext().getFont();
        
        if (result) {
            font.draw(batch, "You won!", maxX / 2, maxY / 2);
        }
        else {
            font.draw(batch, "You lost!", maxX / 2, maxY / 2);
        }
        
        String player = statistics.getPlayers().get(0);
        font.draw(batch, "You took " + statistics.getStatFromPlayer(player, Statistic.DAMAGE_TAKEN) + " damage", maxX / 2, maxY / 4);
        
        font.draw(batch, "You dealt " + statistics.getStatFromPlayer(player, Statistic.DAMAGE_DEALT) + " damage", maxX / 2, maxY / 3);
        
        font.draw(batch, "You regained " + statistics.getStatFromPlayer(player, Statistic.HEALTH_REGAINED) + " health", maxX / 2, maxY / 5);
        
        font.draw(batch, "You fired " + statistics.getStatFromPlayer(player, Statistic.GUNSHOTS_FIRED) + " shot" + (statistics.getStatFromPlayer(player, Statistic.GUNSHOTS_FIRED) == 1 ? "" : "s"), maxX / 2, maxY / 6);
        
        font.draw(batch, "You used " + statistics.getStatFromPlayer(player, Statistic.TELEPORTS_USED) + " teleporter" + (statistics.getStatFromPlayer(player, Statistic.TELEPORTS_USED) == 1 ? "" : "s"), maxX / 2, maxY / 7);
        
        batch.end();
    }
    
    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            getStateManager().popState();
        }
    }
    
    @Override
    public void dispose() {
    }
}
