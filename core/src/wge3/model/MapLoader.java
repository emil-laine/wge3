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
import wge3.engine.util.Direction;
import wge3.model.actors.*;
import wge3.model.items.*;
import wge3.model.grounds.*;
import wge3.model.objects.*;

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
                    case 1: ground = new Grass(); break;
                    case 2: ground = new WoodenFloor(); break;
                    case 3: ground = new Water(); break;
                    case 4: ground = new Lava(); break;
                    
                    case 5: ground = new OneWayFloor(Direction.UP); break;
                    case 6: ground = new OneWayFloor(Direction.RIGHT); break;
                    case 7: ground = new OneWayFloor(Direction.DOWN); break;
                    case 8: ground = new OneWayFloor(Direction.LEFT); break;
                    
                    case 21: ground = new Stone(); break;
                    
                    default:ground = new Abyss(); break;
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
                MapObject object;
                switch (mapLoader.nextInt()) {
                    case 0:  object = null; break;
                    case 9:  object = new BrickWall(); break;
                    case 12: object = new StoneWall(0); break;
                    case 13: object = new StoneWall(1); break;
                    case 14: object = new StoneWall(2); break;
                    case 15: object = new Tree(); break;
                        
                    case 17: object = new BasicBomb(); break;
                    case 18: object = new Handgun(); break;
                    case 20: object = new HealthPack(); break;
                    
                    case 23:
                        object = null;
                        area.addCreature(new Zombie(), x, y);
                        break;
                        
                    case 31: object = new Door(false, true); break;
                    case 32: object = new Door(true, true); break;
                    case 39: object = new Door(false, false); break;
                    case 40: object = new Door(true, false); break;
                        
                    case 25:
                        object = null;
                        area.addCreature(new Player(), x, y);
                        playersAdded++;
                        break;
                        
                    case 26:
                        object = null;
                        area.addCreature(new Gunman(), x, y);
                        break;
                        
                    case 33: object = new GreenPotion(); break;
                    case 34: object = new GreenSlime(); area.addSlime((GreenSlime) object); break;
                    case 35: object = new Teleport(Color.RED); break;
                    case 36: object = new Teleport(Color.BLUE); break;
                    case 37: object = new Teleport(Color.GREEN); break;
                    case 38: object = new Teleport(Color.BLACK); break;
                        
                    case 41: object = new SpeedPotion(); break;
                    case 42: object = new LevitationPotion(); break;
                    case 43:
                        object = null;
                        area.addCreature(new Thief(), x, y);
                        break;
                    case 44: object = new InvisibilityPotion(); break;
                        
                    default: object = null; break;
                }
                if (object != null) {
                    area.getTileAt(x, y).setObject(object);
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
