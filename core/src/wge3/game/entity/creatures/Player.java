package wge3.game.entity.creatures;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.game.engine.constants.StateFlag;
import static wge3.game.engine.constants.Team.PlayerTeam;
import wge3.game.engine.utilities.Statistics;

public final class Player extends Creature {
    
    private OrthographicCamera camera;
    private String name;
    public static Statistics statistics;
    
    public Player() {
        setSprite(4, 3);
        team = PlayerTeam;
        size = 14;
        HP.setMax(100);
        strength = 10;
        defense = 5;
        
        stateFlags.add(StateFlag.PICKS_UP_ITEMS);
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

    public String getName() {
        return name;
    }
    
    public void setStats(Statistics statistics) {
        Player.statistics = statistics;
    }
    
    public static Statistics getStats() {
        return statistics;
    }
}
