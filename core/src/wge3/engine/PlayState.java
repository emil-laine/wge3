/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import wge3.gui.HUD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;
import wge3.model.Area;
import wge3.model.Creature;
import wge3.model.MapObject;
import wge3.model.NonPlayer;
import wge3.model.Player;

public final class PlayState extends GameState {
    
    private HUD hud;
    private OrthographicCamera camera;
    private final float zoomAmount = 1.05f;
    private Rectangle playerViewport;
    private Statistics statistics;
    
    private Area area;
    private Player player;
    public static MessageStream mStream;
    
    public PlayState(String map) {
        this.statistics = new Statistics();
        
        playerViewport = new Rectangle(0, 0,
                                       getGraphicsContext().getLogicalWidth(),
                                       getGraphicsContext().getLogicalHeight());
        camera = new OrthographicCamera(playerViewport.width, playerViewport.height);
        
        mStream = new MessageStream(getGraphicsContext(),
                                    getGraphicsContext().getLogicalWidth() - 280,
                                    getGraphicsContext().getLogicalHeight() - 60,
                                    this);
        area = new Area(map);
        player = area.getPlayers().get(0);
        player.setStats(statistics);
        player.setCamera(camera);
        hud = new HUD(player, getGraphicsContext());
        
        camera.translate(player.getX(), player.getY());
        camera.update();
    }
    
    @Override
    public void enter() {
        Gdx.input.setInputProcessor(getInputHandler());
    }
    
    public Player getPlayer() {
        return player;
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
                creature.die();
                it.remove();
                area.removeCreature(creature);
                
                if (area.getPlayers().isEmpty()) {
                    getStateManager().popState();
                    getStateManager().pushState(new EndGameState(false, statistics));
                    return;
                }
                
                if (area.getNPCs().isEmpty()) {
                    getStateManager().popState();
                    getStateManager().pushState(new EndGameState(true, statistics));
                    return;
                }
            }
        }
        area.calculateFOV();
        area.calculateLighting();
        area.passTime(delta);
        handleInput();
        getInputHandler().copyKeyBuffer();
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
        InputHandler input = getInputHandler();
        
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
            getStateManager().popState();
        else if (input.isPressed(Command.TOGGLE_FOV))
            player.toggleSeeEverything();
        else if (input.isPressed(Command.TOGGLE_GHOST_MODE))
            player.toggleGhostMode();
        else if (input.isPressed(Command.TOGGLE_INVENTORY))
            mStream.toggleShowInventory();
        else if (input.isPressed(Command.SPAWN_WALL))
            area.getTileAt(player.getX(), player.getY()).setObject(new MapObject("stoneWall"));
        else if (input.isPressed(Command.DESTROY_OBJECT))
            area.getTileAt(player.getX(), player.getY()).removeObject();
        else if (input.isPressed(Command.TOGGLE_FPS))
            mStream.toggleShowFPS();
        else if (input.isPressed(Command.TOGGLE_MUSIC))
            Audio.toggleMusic();
        else if (input.isDown(Command.ZOOM_IN))
            zoomIn();
        else if (input.isDown(Command.ZOOM_OUT))
            zoomOut();
        else if (input.isDown(Command.ZOOM_RESET))
            zoomReset();
    }
    
    private void zoomIn() {
        camera.zoom *= zoomAmount;
        camera.update();
    }
    
    private void zoomOut() {
        camera.zoom /= zoomAmount;
        camera.update();
    }
    
    private void zoomReset() {
        camera.zoom = 1;
        camera.update();
    }
    
    @Override
    public void dispose() {
    }
}
