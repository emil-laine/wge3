package wge3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.TimeUtils;
import wge3.entity.terrainelement.Item;
import wge3.interfaces.Drawable;

public final class MessageStream implements Drawable {

    private PlayState game;
    
    private BitmapFont font;
    private int x;
    private int y;
    private int lineSpacing;
    
    private String latestMessage;
    private long latestMessageTime;
    
    private boolean showFPS;
    private boolean showInventory;

    public MessageStream(int x, int y, PlayState game) {
        font = new BitmapFont();
        this.x = x;
        this.y = y;
        lineSpacing = 20;
        this.game = game;
        latestMessageTime = TimeUtils.millis();
    }

    @Override
    public void draw(Batch batch) {
        int line = 0;
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
        if (latestMessage != null) {
            font.draw(batch, latestMessage, x, y - line*lineSpacing);
            if (TimeUtils.timeSinceMillis(latestMessageTime) > 5000) {
                latestMessage = null;
            }
        }
    }

    @Override
    public boolean needsToBeDrawn() {
        return true;
    }
    
    public void toggleShowFPS() {
        showFPS = showFPS == false;
    }
    
    public void toggleShowInventory() {
        showInventory = showInventory == false;
    }
    
    public void addMessage(String message) {
        latestMessage = message;
        latestMessageTime = TimeUtils.millis();
    };
}