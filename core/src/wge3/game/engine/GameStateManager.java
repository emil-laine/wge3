package wge3.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import wge3.game.engine.gamestates.EndGameState;
import wge3.game.engine.gamestates.MenuState;
import wge3.game.engine.gamestates.PlayState;
import wge3.game.engine.utilities.Statistics;

public final class GameStateManager {
    
    public static int WIDTH = Gdx.graphics.getWidth();
    public static int HEIGHT = Gdx.graphics.getHeight();
    
    private GameState currentState;
    private SpriteBatch batch;
    private Statistics statistics;
    
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
                EndGameState endgamestate = new EndGameState(this, gameResult);
                endgamestate.setStatistics(statistics);
                currentState = endgamestate;
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
    
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
