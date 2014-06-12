package wge3.gamestate;

import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.entity.character.Player;
import wge3.world.Area;

public class PlayState extends GameState {
    
    private Area area;
    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        area = new Area();
        player = new Player(area);
    }

    @Override
    public void update(float delta) {
        handleInput();
        input.updateKeyDowns();
        player.updatePosition(delta);
        //System.out.println(Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        area.draw(batch);
        player.draw(batch);
        batch.end();
        needsToBeDrawn = false;
    }

    @Override
    public void handleInput() {
        player.goForward (input.isDown(0));
        player.goBackward(input.isDown(1));
        player.turnLeft  (input.isDown(2));
        player.turnRight (input.isDown(3));
        if (input.isPressed(4)) {
            player.useItem();
        }
    }

    @Override
    public void dispose() {
        // code...
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }
}
