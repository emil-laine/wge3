/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import wge3.engine.util.Color;

public final class MapLoader {
    
    public static void loadMap(String mapName, Area area) throws FileNotFoundException, IOException {
        int w = getWidth(mapName);
        int h = getHeight(mapName);
        area.createTiles(w, h);
        Tile.setArea(area);
        
        Scanner mapLoader = new Scanner(new File("maps/" + mapName + ".tmx"));
        mapLoader.useDelimiter("[," + getLineSeparator() + "]");
        
        for (int i = 0; i < 7; i++) mapLoader.nextLine();
        
        // Create tiles and load grounds:
        for (int y = h-1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {
                Ground ground;
                switch (mapLoader.nextInt()) {
                    case 1:  ground = new Ground("grass"); break;
                    case 2:  ground = new Ground("woodenFloor"); break;
                    case 3:  ground = new Ground("water"); break;
                    case 4:  ground = new Ground("lava"); break;
                    case 21: ground = new Ground("stone"); break;
                    case 22:
                    default: ground = new Ground("abyss"); break;
                }
                Tile newtile = new Tile();
                newtile.setGround(ground);
                area.addTile(newtile, x, y);
            }
            mapLoader.nextLine();
        }
        for (int i = 0; i < 4; i++) mapLoader.nextLine();
        
        int playersAdded = 0;
        
        // Load objects, items and creatures:
        for (int y = h-1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {
                MapObject object = null;
                ItemInstance item = null;
                
                switch (mapLoader.nextInt()) {
                    case 0:  object = null; break;
                    case 9:  object = new MapObject("brickWall"); break;
                    case 12: object = new MapObject("stoneWall", 0); break;
                    case 13: object = new MapObject("stoneWall", 1); break;
                    case 14: object = new MapObject("stoneWall", 2); break;
                    case 15: object = new MapObject("tree"); break;
                        
                    case 17: item = new ItemInstance("bomb"); break;
                    case 18: item = new ItemInstance("handgun"); break;
                    case 20: item = new ItemInstance("healthPack"); break;
                    
                    case 23:
                        object = null;
                        area.addCreature(new NonPlayer("zombie"), x, y);
                        break;
                        
                    case 31: object = new MapObject("door", false, true); break;
                    case 32: object = new MapObject("door", true, true); break;
                    case 39: object = new MapObject("door", false, false); break;
                    case 40: object = new MapObject("door", true, false); break;
                        
                    case 25:
                        object = null;
                        area.addCreature(new Player(), x, y);
                        playersAdded++;
                        break;
                        
                    case 26:
                        object = null;
                        area.addCreature(new NonPlayer("gunman"), x, y);
                        break;
                        
                    case 33: item = new ItemInstance("greenPotion"); break;
                    case 34: object = new MapObject("greenSlime"); break;
                    case 35: object = new MapObject("teleport", Color.RED); break;
                    case 36: object = new MapObject("teleport", Color.BLUE); break;
                    case 37: object = new MapObject("teleport", Color.GREEN); break;
                    case 38: object = new MapObject("teleport", Color.BLACK); break;
                        
                    case 41: item = new ItemInstance("speedPotion"); break;
                    case 43:
                        object = null;
                        area.addCreature(new NonPlayer("thief"), x, y);
                        break;
                    case 44: item = new ItemInstance("invisibilityPotion"); break;
                }
                if (object != null) {
                    area.getTileAt(x, y).setObject(object);
                }
                if (item != null) {
                    area.addItem(item, x, y);
                }
            }
            mapLoader.nextLine();
        }
        
        if (playersAdded == 0) {
            area.addCreature(new Player());
        }
        
        mapLoader.close();
    }
    
    public static int getWidth(String mapName) throws IOException {
        Element mapData = new XmlReader().parse(Gdx.files.internal("maps/" + mapName + ".tmx"));
        return Integer.parseInt(mapData.getAttribute("width"));
    }
    
    public static int getHeight(String mapName) throws IOException {
        Element mapData = new XmlReader().parse(Gdx.files.internal("maps/" + mapName + ".tmx"));
        return Integer.parseInt(mapData.getAttribute("height"));
    }
    
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }
    
    private MapLoader() {}
}
