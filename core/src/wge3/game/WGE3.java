package wge3.game;

import wge3.gamestate.GameStateManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WGE3 extends Game {
    
    public int WIDTH;
    public int HEIGHT;
    
    public OrthographicCamera cam;
    public GameStateManager gsm;

    @Override
    public void create () {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.translate(WIDTH/2, HEIGHT/2);
        cam.update();
        
        gsm = new GameStateManager();
        gsm.setState(1);
    }

    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gsm.draw();
    }
}
