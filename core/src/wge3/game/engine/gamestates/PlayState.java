package wge3.game.engine.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;
import wge3.game.engine.GameState;
import wge3.game.engine.GameStateManager;
import static wge3.game.engine.GameStateManager.HEIGHT;
import static wge3.game.engine.GameStateManager.WIDTH;
import wge3.game.engine.InputHandler;
import wge3.game.engine.Command;
import wge3.game.engine.gui.HUD;
import wge3.game.engine.gui.MessageStream;
import wge3.game.engine.utilities.Statistics;
import wge3.game.entity.Area;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.tilelayers.mapobjects.walls.StoneWall;

public final class PlayState extends GameState {
    
    private HUD hud;
    private OrthographicCamera camera;
    private Rectangle playerViewport;
    private Statistics statistics;
    
    private Area area;
    private Player player;
    public static MessageStream mStream;
    private String map;
    
    public PlayState(GameStateManager gsm, String map) {
        super(gsm);
        this.statistics = new Statistics();
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
        
        mStream = new MessageStream(WIDTH - 280, HEIGHT - 60, this);
        area = new Area(map);
        player = area.getPlayers().get(0);
        player.setStats(statistics);
        player.setCamera(camera);
        hud = new HUD(player);
        
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
                    gsm.setStatistics(statistics);
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
        input.copyKeyBuffer();
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
        if (input.isDown(Command.RUN))
            player.startRunning();
        else if (!input.isDown(Command.RUN))
            player.stopRunning();
        
        if (input.isDown(Command.FORWARD))
            player.goForward();
        else if (input.isDown(Command.BACKWARD))
            player.goBackward();
        
        if (input.isDown(Command.TURN_LEFT))
            player.turnLeft();
        else if (input.isDown(Command.TURN_RIGHT))
            player.turnRight();
        
        if (input.isPressed(Command.USE_ITEM))
            player.useItem();
        
        if (input.isPressed(Command.CHANGE_ITEM))
            player.changeItem();
        else if (input.isPressed(Command.EXIT))
            gsm.setState(0);
        else if (input.isPressed(Command.TOGGLE_FOV))
            player.toggleSeeEverything();
        else if (input.isPressed(Command.TOGGLE_GHOST_MODE))
            player.toggleGhostMode();
        else if (input.isPressed(Command.TOGGLE_INVENTORY))
            mStream.toggleShowInventory();
        else if (input.isPressed(Command.SPAWN_WALL))
            area.getTileAt(player.getX(), player.getY()).setObject(new StoneWall(random(2)));
        else if (input.isPressed(Command.DESTROY_OBJECT))
            area.getTileAt(player.getX(), player.getY()).removeObject();
        else if (input.isPressed(Command.TOGGLE_FPS))
            mStream.toggleShowFPS();
    }
    
    @Override
    public void dispose() {
        area.dispose();
        mStream = null;
        area = null;
        player = null;
    }
}
