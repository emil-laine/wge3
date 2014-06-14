package wge3.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wge3.entity.character.Player;
import wge3.entity.mapobject.BrickWall;
import wge3.world.Area;

public class PlayState extends GameState {
    
    private Area area;
    private Player player;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        try {
            area = new Area();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayState.class.getName()).log(Level.SEVERE, null, ex);
        }
        player = new Player();
        area.addCreature(player);
    }

    @Override
    public void update(float delta) {
        handleInput();
        input.updateKeyDowns();
        player.updatePosition(delta);
        area.calculateFOV();
        //System.out.println(Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        batch.disableBlending();
        area.draw(batch);
        batch.enableBlending();
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
        } else if (input.isPressed(6)) {
            player.toggleCanSeeEverything();
        } else if (input.isPressed(7)) {
            player.toggleWalksThroughWalls();
        } else if (input.isPressed(8)) {
            player.getInventory().print();
        } else if (input.isPressed(9)) {
            area.getTileAt(player.getX(), player.getY()).setObject(new BrickWall());
        } else if (input.isPressed(10)) {
            area.getTileAt(player.getX(), player.getY()).removeObject();
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
