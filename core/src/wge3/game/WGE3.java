package wge3.game;

import wge3.gamestate.GameStateManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class WGE3 extends Game {
    
    public int WIDTH;
    public int HEIGHT;
    
    public GameStateManager gsm;

    @Override
    public void create () {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        
        gsm = new GameStateManager();
        gsm.setState(1);
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.draw();
    }
}
