package wge3.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.mapobjects.StoneWall;
import wge3.game.GameStateManager;
import wge3.game.HUD;
import wge3.game.InputHandler;
import wge3.game.MessageStream;
import wge3.world.Area;

public final class PlayState extends GameState {
    
    private HUD hud;
    private OrthographicCamera camera;
    private Rectangle playerViewport;
    
    private Area area;
    private Player player;
    public static MessageStream mStream;
    private String map;

    public PlayState(GameStateManager gsm, String map) {
        super(gsm);
        this.map = map;
        init();
    }

    public InputHandler getInput() {
        return input;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void init() {
        playerViewport = new Rectangle(340, 60, 600, 600);
        camera = new OrthographicCamera(playerViewport.width, playerViewport.height);
        hud = new HUD();
        
        mStream = new MessageStream(Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 10, this);
        area = new Area(map);
        player = area.getPlayers().get(0);
        player.setCamera(camera);
        
        camera.translate(player.getX(), player.getY());
        camera.update();
    }

    @Override
    public void update(float delta) {
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
                
                if (area.getPlayers().isEmpty()) {
                    gsm.setGameEnd(false);
                    gsm.setState(2);
                    return;
                }
                
                if (area.getNPCs().isEmpty()) {
                    gsm.setGameEnd(true);
                    gsm.setState(2);
                    return;
                }
            }
            
            
        }
        area.calculateFOV();
        area.calculateLighting();
        area.passTime(delta);
        handleInput();
        input.updateKeyDowns();
    }

    @Override
    public void draw(Batch batch) {
        Gdx.graphics.getGL20().glViewport(
                (int) playerViewport.x,
                (int) playerViewport.y,
                (int) playerViewport.width,
                (int) playerViewport.height);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.disableBlending();
        area.draw(batch);
        batch.enableBlending();
        hud.draw(batch);
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
            gsm.setState(0);
        } else if (input.isPressed(7)) {
            player.toggleCanSeeEverything();
        } else if (input.isPressed(8)) {
            player.toggleGhostMode();
        } else if (input.isPressed(9)) {
            mStream.toggleShowInventory();
        } else if (input.isPressed(10)) {
            area.getTileAt(player.getX(), player.getY()).setObject(new StoneWall(random(2)));
        } else if (input.isPressed(11)) {
            area.getTileAt(player.getX(), player.getY()).removeObject();
        } else if (input.isPressed(12)) {
            mStream.toggleShowFPS();
        }
    }

    @Override
    public void dispose() {
        area.dispose();
        mStream = null;
        area = null;
        player = null;
    }
}
