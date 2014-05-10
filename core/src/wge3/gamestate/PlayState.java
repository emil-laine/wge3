package wge3.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import wge3.world.Area;
import wge3.entity.character.Player;

public class PlayState extends GameState {
    
    private ShapeRenderer sr;
    private Area area;
    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        sr = new ShapeRenderer();
        area = new Area();
        player = new Player(area);
        //area.draw(sr);
    }

    @Override
    public void update(float delta) {
        
        handleInput();
        input.updateKeyDowns();
        player.updatePosition(delta);
        System.out.println(Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void draw() {
        if (area.needsToBeDrawn()) {
            area.draw(sr);
            //area.setNeedsToBeDrawn(false);
        }
        player.draw(sr);
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
    }
}
