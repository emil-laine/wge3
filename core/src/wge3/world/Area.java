package wge3.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import wge3.entity.character.Creature;
import wge3.entity.ground.Grass;
import wge3.entity.ground.Water;
import wge3.entity.ground.WoodenFloor;
import wge3.entity.object.BrickWall;
import wge3.interfaces.Drawable;

public class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private boolean needsToBeDrawn;
    private List<Tile> allTiles;
    private List<Tile> tilesToDraw;
    private List<Creature> creatures;

    public Area() throws FileNotFoundException {
        size = 31;
        map = new Tile[size][size];
        allTiles = new LinkedList<Tile>();
        tilesToDraw = new LinkedList<Tile>();
        creatures = new LinkedList<Creature>();
        
        // Generate map:
        File mapFile = new File("maps/testmap.txt");
        Scanner mapLoader = new Scanner(mapFile);
        for (int i = 0; i < 8; i++) {
            mapLoader.nextLine();
        }

        // Create tiles and load grounds:
        for (int i = 0; i < size; i++) {
            String line = mapLoader.nextLine();
            
            for (int j = 0; j < size; j++) {
                
                Ground ground;
                switch (line.charAt(2*j)) {
                    case '~': ground = new Water(); break;
                    case '_': ground = new WoodenFloor(); break;
                    default:  ground = new Grass(); break;
                }
                
                Tile newtile = new Tile();
                newtile.setGround(ground);
                newtile.setX(j);
                newtile.setY(size-1 - i);
                allTiles.add(newtile);
                map[j][size-1 - i] = newtile;
            }
        }
        
        for (int i = 0; i < 3; i++) mapLoader.nextLine();
        
        // Load objects:
        for (int i = 0; i < size; i++) {
            String line = mapLoader.nextLine();
            
            for (int j = 0; j < size; j++) {
                MapObject object;
                switch (line.charAt(2*j)) {
                    case 'w': object = new BrickWall(); break;
                    default: object = null; break;
                }
                
                if (object != null) {
                    map[j][size-1 - i].setObject(object);
                }
            }
        }
        
        mapLoader.close();
        needsToBeDrawn = true;
    }
    
    public Tile[][] getMap() {
        return map;
    }

    public int getSize() {
        return size;
    }
    
    @Override
    public void draw(Batch batch) {
        for (Iterator<Tile> it = tilesToDraw.iterator(); it.hasNext();) {
            Tile tile = it.next();
            tile.draw(batch);
            it.remove();
        }
        
        needsToBeDrawn = false;
    }
    
    public void forceDraw(Batch batch) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[j][i].draw(batch);
            }
        }
    }
    
    public Tile getTileAt(float x, float y) {
        int x0 = (int) ((x - (x % Tile.size)) / Tile.size);
        int y0 = (int) ((y - (y % Tile.size)) / Tile.size);
        
        if (x0 < 0 || x0 >= size || y0 < 0 || y0 >= size) {
            return null;
        }
        
        return map[x0][y0];
    }

    @Override
    public boolean needsToBeDrawn() {
        // Area needs to be redrawn if any of the tiles in it needs to be redrawn.
        return needsToBeDrawn;
    }
    
    public void requestDrawTile(float x, float y) {
        if (getTileAt(x, y) != null) {
            tilesToDraw.add(getTileAt(x, y));
        }
    }
    
    public void checkLOS(Creature c) {
        if (!c.canSeeEverything()) {
            for (Tile tile : allTiles) {
                if (tile.canBeSeenBy(c)) {
                    tilesToDraw.add(tile);
                }
            }
        } else {
            for (Tile tile : allTiles) {
                tilesToDraw.add(tile);
            }
        }
    }

    public void addCreature(Creature c) {
        c.setArea(this);
        creatures.add(c);
    }
}
