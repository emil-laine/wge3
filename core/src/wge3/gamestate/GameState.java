package wge3.gamestate;

import com.badlogic.gdx.Gdx;
import wge3.game.InputHandler;

public abstract class GameState {
    
    protected GameStateManager gsm;
    protected InputHandler input;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        input = new InputHandler();
        Gdx.input.setInputProcessor(input);
        init();
    }
    
    public abstract void init();
    public abstract void update(float delta);
    public abstract void draw();
    public abstract void handleInput();
    public abstract void dispose();
}
