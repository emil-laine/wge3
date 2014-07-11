package wge3.game.entity.creatures;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static wge3.game.engine.constants.Team.PlayerTeam;

public final class Player extends Creature {
    
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private boolean showHP;
    
    public Player() {
        setSprite(4, 3);
        team = PlayerTeam;
        size = 14;
        HP.setMaximum(100);
        strength = 10;
        defense = 5;
        
        picksUpItems = true;
        showHP = true;
        shapeRenderer = new ShapeRenderer();
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
    
    @Override
    public void move(float dx, float dy) {
        float oldX = getX();
        float oldY = getY();
        super.move(dx, dy);
        camera.translate(getX() - oldX, getY() - oldY);
        camera.update();
    }
    
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
