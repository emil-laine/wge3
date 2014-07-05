package wge3.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class GameStateManager {
    
    private GameState currentState;
    //true means win, false means lose
    private boolean gameResult;
    private SpriteBatch batch;

    public GameStateManager() {
        batch = new SpriteBatch();
    }
    
    public void setState(int newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        switch (newState) {
            case 0:
                // menu
                break;
                
            case 1:
                currentState = new PlayState(this);
                break;
                
            case 2:
                // preferences
                currentState = new EndGameState(this, gameResult);
                break;
        }
    }
    
    public void update(float delta) {
        currentState.update(delta);
    }
    
    public void draw() {
        currentState.draw(batch);
    }
    //true means win, false means lose
    public void setGameEnd(boolean result) {
        this.gameResult = result;
    }
}
