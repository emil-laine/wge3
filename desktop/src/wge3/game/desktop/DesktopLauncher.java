/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import wge3.game.engine.WGE3;

public class DesktopLauncher {
    
    public static void main (String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        
        cfg.width = 1280;
        cfg.height = 720;
        //cfg.fullscreen = true;
        cfg.resizable = false;
        cfg.title = "WGE3";
        cfg.useGL30 = false;
        
        LwjglApplication game = new LwjglApplication(new WGE3(), cfg);
    }
}
