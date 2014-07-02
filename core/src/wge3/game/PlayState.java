package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.random;
import java.util.Iterator;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.mapobjects.StoneWall;
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
        for (NonPlayer NPC : area.getNPCs()) {
            NPC.updateAI();
        }
        for (Iterator<Creature> it = area.getCreatures().iterator(); it.hasNext();) {
            Creature creature = it.next();
            creature.doMovement(delta);
            
            // Clean up the dead:
            if (creature.isDead()) {
                it.remove();
                area.removeCreature(creature);
            }
        }
        area.calculateFOV();
        area.calculateLighting();
        area.passTime(delta);
        input.updateKeyDowns();
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
        if (input.isDown(0)) player.goForward();
        else if (input.isDown(1)) player.goBackward();
        
        if (input.isDown(2)) player.turnLeft();
        else if (input.isDown(3)) player.turnRight();
        
        if (input.isPressed(4)) {
            player.useItem();
        } else if (input.isPressed(5)) {
            player.changeItem();
        } else if (input.isPressed(6)) {
            player.toggleCanSeeEverything();
        } else if (input.isPressed(7)) {
            player.toggleWalksThroughWalls();
            if (player.walksThroughWalls()) mStream.addMessage("Ghost Mode On");
            else mStream.addMessage("Ghost Mode Off");
        } else if (input.isPressed(8)) {
            mStream.toggleShowInventory();
        } else if (input.isPressed(9)) {
            area.getTileAt(player.getX(), player.getY()).setObject(new StoneWall(random(2)));
        } else if (input.isPressed(10)) {
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
