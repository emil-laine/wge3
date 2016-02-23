/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

public enum Command {
    FORWARD(0),
    BACKWARD(1),
    TURN_LEFT(2),
    TURN_RIGHT(3),
    USE_ITEM(4),
    CHANGE_ITEM(5),
    UNUSED(6),
    TOGGLE_FPS(7),
    EXIT(8),
    TOGGLE_FOV(9),
    TOGGLE_GHOST_MODE(10),
    TOGGLE_INVENTORY(11),
    SPAWN_WALL(12),
    DESTROY_OBJECT(13),
    TOGGLE_MUSIC(14);
    
    public final int code;
    public static final int numberOfCommands = 15;
    
    private Command(int code) {
        this.code = code;
    }
}
