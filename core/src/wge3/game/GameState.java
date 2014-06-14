package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.game.InputHandler;
import wge3.interfaces.Drawable;

public abstract class GameState implements Drawable {
    
    protected GameStateManager gsm;
    protected InputHandler input;
    
    protected boolean needsToBeDrawn;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        input = new InputHandler();
        Gdx.input.setInputProcessor(input);
        
        init();
        needsToBeDrawn = true;
    }
    
    public abstract void init();
    public abstract void update(float delta);
    @Override
    public abstract void draw(Batch batch);
    public abstract void handleInput();
    public abstract void dispose();

    public boolean NeedsToBeDrawn() {
        return needsToBeDrawn;
    }
}
