/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author chang
 */

//Remember to implement statistics


public class EndGameState extends GameState {

    private BitmapFont font;
    private boolean result;
    
    public EndGameState(GameStateManager gsm, boolean result) {
        super(gsm);
        font = new BitmapFont();
        this.result = result;
    }

    @Override
    public void init() {
        
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void draw(Batch batch) {
        int maxX = Gdx.graphics.getWidth(); 
        int maxY = Gdx.graphics.getHeight();
        if (result) {
            font.draw(batch, "You won!", maxX / 2, maxY / 2);
        }
        else {
            font.draw(batch, "You lost!", maxX / 2, maxY / 2);
        }
        
    }

    @Override
    public void handleInput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
