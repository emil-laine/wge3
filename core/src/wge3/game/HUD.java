package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import static wge3.game.GameStateManager.HEIGHT;
import static wge3.game.GameStateManager.WIDTH;

public final class HUD implements Drawable {
    
    private OrthographicCamera camera;
    private Texture texture;

    public HUD() {
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.translate(WIDTH/2, HEIGHT/2);
        camera.update();
        texture = new Texture(Gdx.files.internal("graphics/frame.png"));
    }

    @Override
    public void draw(Batch batch) {
        Gdx.graphics.getGL20().glViewport(
                0,
                0,
                WIDTH,
                HEIGHT);
        batch.setProjectionMatrix(camera.combined);
        batch.draw(texture, 0, 0);
    }
}
