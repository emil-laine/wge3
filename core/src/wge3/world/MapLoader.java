package wge3.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import wge3.entity.character.Player;
import wge3.entity.creatures.Zombie;
import wge3.entity.ground.Direction;
import wge3.entity.ground.Grass;
import wge3.entity.ground.Lava;
import wge3.entity.ground.OneWayTile;
import wge3.entity.ground.Water;
import wge3.entity.ground.WoodenFloor;
import wge3.entity.items.Bomb;
import wge3.entity.items.Handgun;
import wge3.entity.mapobjects.BrickWall;
import wge3.entity.mapobjects.StoneWall;
import wge3.entity.terrainelements.Ground;
import wge3.entity.terrainelements.MapObject;

public final class MapLoader {
    
    private Scanner mapFileReader;
    
    public void loadMap(String mapName, Area area) throws FileNotFoundException {
        int size = area.getSize();
        String newline = System.getProperty("line.separator");
        System.out.println(newline);
        Scanner mapLoader = new Scanner(new File("maps/" + mapName + ".tmx"));
        mapLoader.useDelimiter("[,\n]");
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
                        
                    case 5: ground = new OneWayTile(Direction.UP); break;
                    case 6: ground = new OneWayTile(Direction.RIGHT); break;
                    case 7: ground = new OneWayTile(Direction.DOWN); break;
                    case 8: ground = new OneWayTile(Direction.LEFT); break;
                                    
                    default:ground = new Grass(); break;
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
                        
                    case 17: object = new Bomb(); break;
                    case 18: object = new Handgun(); break;
                    case 23: object = null; area.addCreature(new Zombie(), x, y); break;
                        
                    case 25: object = null; area.addCreature(new Player(), x, y); break;
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
}
