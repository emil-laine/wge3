package wge3.game.engine.gui;

import wge3.game.engine.gamestates.PlayState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import wge3.game.entity.tilelayers.mapobjects.Item;

public final class MessageStream implements Drawable {

    private PlayState game;
    
    private BitmapFont font;
    private int x;
    private int y;
    private int lineSpacing;
    
    private Map<Long, String> messages;
    
    private boolean showFPS;
    private boolean showInventory;

    public MessageStream(int x, int y, PlayState game) {
        font = new BitmapFont();
        this.x = x;
        this.y = y;
        lineSpacing = 20;
        this.game = game;
        
        messages = new LinkedHashMap<Long, String>();
    }

    @Override
    public void draw(Batch batch) {
        int line = 0;
        font.draw(batch, "HP: " + game.getPlayer().getHP() + "/" + game.getPlayer().getMaxHP(), x, y - line*lineSpacing);
        line++;
        if (game.getPlayer().getSelectedItem() != null) {
            font.draw(batch, "Selected weapon: " + game.getPlayer().getSelectedItem().getName() + " (x" + game.getPlayer().getInventory().getAmount(game.getPlayer().getSelectedItem()) + ")", x, y - line*lineSpacing);
        } else {
            font.draw(batch, "Selected weapon: unarmed", x, y - line*lineSpacing);
        }
        line++;
        line++;
        
        if (showFPS) {
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), x, y - line*lineSpacing);
            line++;
        }
        if (showInventory) {
            font.draw(batch, "Inventory:\n", x, y - line*lineSpacing);
            line++;
            for (Item item : game.getPlayer().getInventory().getItems()) {
                font.draw(batch, String.valueOf(game.getPlayer().getInventory().getAmount(item)) + "x " + item.getName(), x, y - line*lineSpacing);
                line++;
            }
        }
        line++;
        
        for (Iterator<Long> it = messages.keySet().iterator(); it.hasNext();) {
            Long time = it.next();
            if (TimeUtils.nanosToMillis(TimeUtils.timeSinceNanos(time)) < 4000) {
                font.draw(batch, messages.get(time), x, y - line*lineSpacing);
                line++;
            } else {
                it.remove();
            }
        }
    }
    
    public void toggleShowFPS() {
        showFPS = showFPS == false;
    }
    
    public void toggleShowInventory() {
        showInventory = showInventory == false;
    }
    
    public void addMessage(String message) {
        messages.put(TimeUtils.nanoTime(), message);
    };
}
