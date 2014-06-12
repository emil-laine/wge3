package wge3.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import wge3.entity.ground.Grass;
import wge3.entity.ground.Ground;
import wge3.entity.ground.Water;
import wge3.entity.ground.WoodenFloor;
import wge3.entity.object.BrickWall;
import wge3.entity.object.MapObject;
import wge3.interfaces.Drawable;

public class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private boolean needsToBeDrawn;
    private List<Tile> tilesToDraw;

    public Area() throws FileNotFoundException {
        size = 31;
        map = new Tile[size][size];
        tilesToDraw = new ArrayList<Tile>();
        /*
        // Temporary hard-coded map file:
        String[] ground_layer = {
          // 0                   1                   2                   3
          // 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", // 00
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . w w w w w w w . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . w _ _ _ _ _ w . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . w _ w _ _ w w . . . . . . . . . ", // 10
            ". . . . . . . . . . . . . . . . w _ w _ _ _ _ . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . w _ w _ _ _ _ . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . w _ _ w w . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . w _ _ _ w . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . w _ _ _ w . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . w w w w w . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", // 20
            ". . . . . . . . . . . . . . . . . . . ~ ~ ~ . . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . . . . ~ ~ ~ . . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", // 30
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . "};//  1
                
        String[] object_layer = {
          // 0                   1                   2                   3
          // 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", // 00
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . w w w w w w w . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . w . . . . . w . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . w . w . . w w . . . . . . . . . ", // 10
            ". . . . . . . . . . . . . . . . w . w . . . . . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . w . w . . . . . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . w . . w w . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . w . . . w . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . w . . . w . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . w w w w w . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . . . . w w w . . . . . . . . . . ", // 20
            ". . . . . . . . . . . . . . . . . . . ~ ~ ~ . . . . . . . . . . ", //  1
            ". . . . . . . . . . . . . . . . . . . ~ ~ ~ . . . . . . . . . . ", //  2
            ". . . . . . . . . . . . . . . . . . . ~ ~ ~ . . . . . . . . . . ", //  3
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  4
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  5
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  6
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  7
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  8
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", //  9
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ", // 30
            ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . "};//  1
        */
        
        File mapFile = new File("maps/testmap.txt");
        
        Scanner mapLoader = new Scanner(mapFile);
        
        //mapLoader.useDelimiter(" ");
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
                
                map[j][size-1 - i] = newtile;
                tilesToDraw.add(map[j][size-1 - i]);
            }
        }
        
        for (int i = 0; i < 3; i++) {
            mapLoader.nextLine();
        }
        
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
        // not optimized: loops through ALL the tiles in the map right now
        /*for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // if (map[j][i].needsToBeDrawn())
                {
                    map[j][i].draw(batch);
                }
            }
        }*/
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
}
