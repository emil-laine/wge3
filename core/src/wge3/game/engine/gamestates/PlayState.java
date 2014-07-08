package wge3.game.engine.gamestates;

import wge3.game.engine.GameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.tilelayers.mapobjects.walls.StoneWall;
import wge3.game.engine.GameStateManager;
import static wge3.game.engine.GameStateManager.HEIGHT;
import static wge3.game.engine.GameStateManager.WIDTH;
import wge3.game.engine.gui.HUD;
import wge3.game.engine.InputHandler;
import wge3.game.engine.gui.MessageStream;
import wge3.game.entity.Area;

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
        
        mStream = new MessageStream(WIDTH - 280, HEIGHT - 60, this);
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
        if (input.isDown(6)) {
            player.startRunning();
        } else if (!input.isDown(6)) {
            player.stopRunning();
        } 
        
        if (input.isDown(0)) player.goForward();
        else if (input.isDown(1)) player.goBackward();
        
        if (input.isDown(2)) player.turnLeft();
        else if (input.isDown(3)) player.turnRight();
        
        if (input.isPressed(4)) {
            player.useItem();
        }
        
        if (input.isPressed(5)) {
            player.changeItem();
        } else if (input.isPressed(7)) {
            gsm.setState(0);
        } else if (input.isPressed(8)) {
            player.toggleCanSeeEverything();
        } else if (input.isPressed(9)) {
            player.toggleGhostMode();
        } else if (input.isPressed(10)) {
            mStream.toggleShowInventory();
        } else if (input.isPressed(11)) {
            area.getTileAt(player.getX(), player.getY()).setObject(new StoneWall(random(2)));
        } else if (input.isPressed(12)) {
            area.getTileAt(player.getX(), player.getY()).removeObject();
        } else if (input.isPressed(13)) {
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
