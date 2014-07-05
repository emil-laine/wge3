/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import wge3.game.GameStateManager;

/**
 *
 * @author chang
 */

//Remember to implement statistics


public final class EndGameState extends GameState {

    private BitmapFont font;
    private boolean result;
    
    public EndGameState(GameStateManager gsm, boolean result) {
        super(gsm);
        font = new BitmapFont();
        this.result = result;
        init();
    }

    @Override
    public void init() {
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        int maxX = Gdx.graphics.getWidth(); 
        int maxY = Gdx.graphics.getHeight();
        if (result) {
            font.draw(batch, "You won!", maxX / 2, maxY / 2);
        }
        else {
            font.draw(batch, "You lost!", maxX / 2, maxY / 2);
        }
        batch.end();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            gsm.setState(0);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
