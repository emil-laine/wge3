package wge3.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {
    
    private final boolean[] keysDown;
    private final boolean[] previousKeysDown;
    
    private final int keys = 9;
    
    private final int up = Keys.UP;
    private final int down = Keys.DOWN;
    private final int left = Keys.LEFT;
    private final int right = Keys.RIGHT;
    
    private final int shoot = Keys.C;
    private final int changeWeapon = Keys.X;
    
    // Development tools:
    private final int toggleFOV = Keys.NUM_1;
    private final int toggleWalkThroughWalls = Keys.NUM_2;
    private final int printInventory = Keys.NUM_3;

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
                
            case shoot: keysDown[4] = true; break;
            case changeWeapon: keysDown[5] = true; break;
                
            case toggleFOV: keysDown[6] = true; break;
            case toggleWalkThroughWalls: keysDown[7] = true; break;
            case printInventory: keysDown[8] = true; break;
                
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
                
            case shoot: keysDown[4] = false; break;
            case changeWeapon: keysDown[5] = false; break;
                
            case toggleFOV: keysDown[6] = false; break;
            case toggleWalkThroughWalls: keysDown[7] = false; break;
            case printInventory: keysDown[8] = false; break;
                
            default: break;
        }
        return true;
    }
}
