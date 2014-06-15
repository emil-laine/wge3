package wge3.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public final class WGE3 extends Game {
    
    private GameStateManager gsm;
    
    @Override
    public void create () {
        gsm = new GameStateManager();
        gsm.setState(1);
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.draw();
    }
}
