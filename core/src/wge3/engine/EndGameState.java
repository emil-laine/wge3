/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
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
    public void update(float delta) {
        handleInput();
    }
    
    @Override
    public void draw(Batch batch) {
        batch.begin();
        int maxX = graphics.getLogicalWidth();
        int maxY = graphics.getLogicalHeight();
        BitmapFont font = graphics.getFont();
        
        if (result) {
            font.draw(batch, "You won!", maxX / 2, maxY / 2);
        }
        else {
            font.draw(batch, "You lost!", maxX / 2, maxY / 2);
        }
        
        String player = statistics.getPlayers().get(0);
        font.draw(batch, "You took " + statistics.getStatFromPlayer(player, Statistic.DAMAGETAKEN) + " damage", maxX / 2, maxY / 4);
        
        font.draw(batch, "You dealt " + statistics.getStatFromPlayer(player, Statistic.DAMAGEDEALT) + " damage", maxX / 2, maxY / 3);
        
        font.draw(batch, "You regained " + statistics.getStatFromPlayer(player, Statistic.HEALTHREGAINED) + " health", maxX / 2, maxY / 5);
        
        font.draw(batch, "You fired " + statistics.getStatFromPlayer(player, Statistic.GUNSHOTSFIRED) + " shot" + (statistics.getStatFromPlayer(player, Statistic.GUNSHOTSFIRED) == 1 ? "" : "s"), maxX / 2, maxY / 6);
        
        font.draw(batch, "You used " + statistics.getStatFromPlayer(player, Statistic.TELEPORTERSUSED) + " teleporter" + (statistics.getStatFromPlayer(player, Statistic.TELEPORTERSUSED) == 1 ? "" : "s"), maxX / 2, maxY / 7);
        
        batch.end();
    }
    
    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            gsm.popState();
        }
    }
    
    @Override
    public void dispose() {
    }
}
