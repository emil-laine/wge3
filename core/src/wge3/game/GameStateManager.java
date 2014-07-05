package wge3.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class GameStateManager {
    
    private GameState currentState;
    private SpriteBatch batch;
    
    private String nextMap;
    private boolean gameResult; // true means win, false means lose

    public GameStateManager() {
        batch = new SpriteBatch();
    }
    
    public void setState(int newState) {
        if (currentState != null) {
            currentState.dispose();
        }
        
        switch (newState) {
            case 0:
                currentState = new MenuState(this);
                break;
                
            case 1:
                currentState = new PlayState(this, nextMap);
                break;
                
            case 2:
                currentState = new EndGameState(this, gameResult);
                break;
        }
    }

    public String getNextMap() {
        return nextMap;
    }

    public void setNextMap(String nextMap) {
        this.nextMap = nextMap;
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
