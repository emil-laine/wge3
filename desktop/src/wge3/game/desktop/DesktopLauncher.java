package wge3.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import wge3.game.WGE3;

public class DesktopLauncher {
    
    public static void main (String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        
        cfg.width = 1024;
        cfg.height = 768;
        //cfg.fullscreen = true;
        cfg.resizable = false;
        cfg.title = "WGE3";
        cfg.useGL30 = false;
        
        LwjglApplication game = new LwjglApplication(new WGE3(), cfg);
    }
}
