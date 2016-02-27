/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import java.util.HashMap;
import java.util.Map;

public final class InputHandler extends InputAdapter {
    
    private final boolean[] currentKeyBuffer;
    private final boolean[] previousKeyBuffer;
    private final int[] keyMap;
    private final Map<Integer, Command> commandMap;
    
    public InputHandler() {
        currentKeyBuffer = new boolean[Command.numberOfCommands];
        previousKeyBuffer = new boolean[Command.numberOfCommands];
        keyMap = new int[Command.numberOfCommands];
        commandMap = new HashMap();
        // Default keys. These should be loaded from a file.
        setMappedKey(Command.UP, Keys.UP);
        setMappedKey(Command.DOWN, Keys.DOWN);
        setMappedKey(Command.LEFT, Keys.LEFT);
        setMappedKey(Command.RIGHT, Keys.RIGHT);
        setMappedKey(Command.USE_ITEM, Keys.Z);
        setMappedKey(Command.CHANGE_ITEM, Keys.X);
        setMappedKey(Command.EXIT, Keys.ESCAPE);
        setMappedKey(Command.TOGGLE_FOV, Keys.S);
        setMappedKey(Command.TOGGLE_GHOST_MODE, Keys.G);
        setMappedKey(Command.TOGGLE_INVENTORY, Keys.I);
        setMappedKey(Command.SPAWN_WALL, Keys.C);
        setMappedKey(Command.DESTROY_OBJECT, Keys.D);
        setMappedKey(Command.TOGGLE_FPS, Keys.F);
        setMappedKey(Command.TOGGLE_MUSIC, Keys.M);
        setMappedKey(Command.ZOOM_IN, Keys.PAGE_DOWN);
        setMappedKey(Command.ZOOM_OUT, Keys.PAGE_UP);
        setMappedKey(Command.ZOOM_RESET, Keys.HOME);
    }
    
    public int getMappedKey(Command key) {
        return keyMap[key.code];
    }
    
    public void setMappedKey(Command key, int mapping) {
        commandMap.remove(mapping);
        commandMap.put(mapping, key);
        keyMap[key.code] = mapping;
    }
    
    public void copyKeyBuffer() {
        System.arraycopy(currentKeyBuffer, 0, previousKeyBuffer, 0, Command.numberOfCommands);
    }
    
    public boolean isDown(Command key) {
        return currentKeyBuffer[key.code];
    }
    
    public boolean isPressed(Command key) {
        return currentKeyBuffer[key.code] && !previousKeyBuffer[key.code];
    }
    
    @Override
    public boolean keyDown(int key) {
        Command command = commandMap.get(key);
        if (command == null) return false;
        currentKeyBuffer[command.code] = true;
        return true;
    }
    
    @Override
    public boolean keyUp(int key) {
        Command command = commandMap.get(key);
        if (command == null) return false;
        currentKeyBuffer[command.code] = false;
        return true;
    }
}
