package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.entity.character.Player;
import wge3.entity.mapobjects.BrickWall;
import wge3.world.Area;

public final class PlayState extends GameState {
    
    private Area area;
    private Player player;
    
    public static MessageStream mStream;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public InputHandler getInput() {
        return input;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void init() {
        mStream = new MessageStream(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 10, this);
        area = new Area();
        player = area.getPlayers().get(0);
    }

    @Override
    public void update(float delta) {
        handleInput();
        input.updateKeyDowns();
        player.updatePosition(delta);
        area.calculateFOV();
        area.calculateLighting();
        area.passTime(delta);
    }

    @Override
    public void draw(Batch batch) {
        batch.begin();
        batch.disableBlending();
        area.draw(batch);
        batch.enableBlending();
        mStream.draw(batch);
        batch.end();
    }

    @Override
    public void handleInput() {
        player.goForward (input.isDown(0));
        player.goBackward(input.isDown(1));
        player.turnLeft  (input.isDown(2));
        player.turnRight (input.isDown(3));
        if (input.isPressed(4)) {
            player.useItem();
        } else if (input.isPressed(5)) {
            player.changeItem();
        } else if (input.isPressed(6)) {
            //player.toggleCanSeeEverything();
        } else if (input.isPressed(7)) {
            player.toggleWalksThroughWalls();
            if (player.walksThroughWalls()) mStream.addMessage("Ghost Mode On");
            else mStream.addMessage("Ghost Mode Off");
        } else if (input.isPressed(8)) {
            mStream.toggleShowInventory();
        } else if (input.isDown(9)) {
            area.getTileAt(player.getX(), player.getY()).setObject(new BrickWall());
        } else if (input.isDown(10)) {
            area.getTileAt(player.getX(), player.getY()).removeObject();
        } else if (input.isPressed(11)) {
            mStream.toggleShowFPS();
        }
    }

    @Override
    public void dispose() {
        // code...
    }
}
