package wge3.game.engine.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.creatures.npcs.Zombie;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;
import wge3.game.engine.constants.Direction;
import wge3.game.entity.tilelayers.grounds.Grass;
import wge3.game.entity.tilelayers.grounds.Lava;
import wge3.game.entity.tilelayers.grounds.OneWayFloor;
import wge3.game.entity.tilelayers.grounds.Water;
import wge3.game.entity.tilelayers.grounds.WoodenFloor;
import wge3.game.entity.tilelayers.mapobjects.items.Bomb;
import wge3.game.entity.tilelayers.mapobjects.items.GreenPotion;
import wge3.game.entity.tilelayers.mapobjects.items.guns.Handgun;
import wge3.game.entity.tilelayers.mapobjects.items.HealthPack;
import wge3.game.entity.tilelayers.mapobjects.walls.BrickWall;
import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.tilelayers.mapobjects.walls.StoneWall;
import wge3.game.entity.tilelayers.mapobjects.Tree;
import wge3.game.entity.tilelayers.Ground;
import wge3.game.entity.tilelayers.MapObject;
import wge3.game.entity.tilelayers.grounds.Abyss;

public final class MapLoader {
    
    public void loadMap(String mapName, Area area) throws FileNotFoundException, IOException {
        int size = getSize(mapName);
        area.createTiles(size);

        Scanner mapLoader = new Scanner(new File("maps/" + mapName + ".tmx"));
        mapLoader.useDelimiter("[," + getLineSeparator() + "]");
        
        for (int i = 0; i < 7; i++) mapLoader.nextLine();
        
        // Create tiles and load grounds:
        for (int y = size-1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
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
                                    
                    default:ground = new Abyss(); break;
                }
                Tile newtile = new Tile();
                newtile.setGround(ground);
                area.addTile(newtile, x, y);
            }
            mapLoader.nextLine();
        }
        for (int i = 0; i < 4; i++) mapLoader.nextLine();
        
        // Load objects:
        for (int y = size-1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
                MapObject object;
                switch (mapLoader.nextInt()) {
                    case 0:  object = null; break;
                    case 9:  object = new BrickWall(); break;
                    case 12: object = new StoneWall(0); break;
                    case 13: object = new StoneWall(1); break;
                    case 14: object = new StoneWall(2); break;
                    case 15: object = new Tree(); break;
                        
                    case 17: object = new Bomb(); break;
                    case 18: object = new Handgun(); break;
                    case 20: object = new HealthPack(); break;
                    case 23: object = null; area.addCreature(new Zombie(), x, y); break;
                        
                    case 25: object = null; area.addCreature(new Player(), x, y); break;
                        
                    case 33: object = new GreenPotion(); break;
                    case 34: object = new GreenSlime(); break;
                    default: object = null; break;
                }
                if (object != null) {
                    area.getTileAt(x, y).setObject(object);
                }
            }
            mapLoader.nextLine();
        }
        mapLoader.close();
    }
    
    public int getSize(String mapName) throws IOException {
        Element mapData = new XmlReader().parse(Gdx.files.internal("maps/" + mapName + ".tmx"));
        return Integer.parseInt(mapData.getAttribute("width"));
        // "width" because WGE3 doesn't support non-square maps yet.
    }
    
    public String getLineSeparator() {
        return System.getProperty("line.separator");
    }
}
