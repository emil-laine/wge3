/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import wge3.engine.util.Drawable;
import wge3.gui.GraphicsContext;
import wge3.model.objects.Item;

public final class MessageStream implements Drawable {
    
    private PlayState game;
    
    private GraphicsContext graphics;
    private int x;
    private int y;
    private int lineSpacing;
    
    private Map<Long, String> messages;
    
    private boolean showFPS;
    private boolean showInventory;
    
    public MessageStream(GraphicsContext graphics, int x, int y, PlayState game) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        lineSpacing = 20;
        this.game = game;
        
        messages = new LinkedHashMap();
    }
    
    @Override
    public void draw(Batch batch) {
        BitmapFont font = graphics.getFont();
        
        int line = 0;
        line++;
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
