package wge3.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {
    
    private boolean[] keysDown;
    private boolean[] previousKeysDown;
    
    private final int keys = 6;
    
    private final int up = Keys.UP;
    private final int down = Keys.DOWN;
    private final int left = Keys.LEFT;
    private final int right = Keys.RIGHT;
    private final int fire = Keys.C;
    private final int change_weapon = Keys.X;
    
    ///////////////////////////
    // 0 = up
    // 1 = down
    // 2 = left
    // 3 = right
    // 
    // 4 = fire/use item
    // 5 = change weapon
    // 6 = run
    ///////////////////////////

    public InputHandler() {
        keysDown = new boolean[keys];
        previousKeysDown = new boolean[keys];
        
        for (int i = 0; i < keys; i++) {
            keysDown[i] = false;
            previousKeysDown[i] = false;
        }
    }
    
    public void updateKeyDowns() {
        System.arraycopy(keysDown, 0, previousKeysDown, 0, keys);
    }
    
    public boolean isDown(int key) {
        return keysDown[key];
    }
    
    public boolean isPressed(int key) {
        return keysDown[key] && !previousKeysDown[key];
    }
    
    @Override
    public boolean keyDown(int key) {
        switch (key) {
            case up:    keysDown[0] = true; break;
            case down:  keysDown[1] = true; break;
            case left:  keysDown[2] = true; break;
            case right: keysDown[3] = true; break;
                
            case fire:  keysDown[4] = true; break;
            case change_weapon: keysDown[5] = true; break;
                
            default: break;
        }
        return true;
    }
    
    @Override
    public boolean keyUp(int key) {
        switch (key) {
            case up:    keysDown[0] = false; break;
            case down:  keysDown[1] = false; break;
            case left:  keysDown[2] = false; break;
            case right: keysDown[3] = false; break;
                
            case fire:  keysDown[4] = false; break;
            case change_weapon: keysDown[5] = false; break;
                
            default: break;
        }
        return true;
    }
}
