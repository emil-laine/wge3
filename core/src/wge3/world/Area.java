package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.ground.Grass;
import wge3.entity.ground.Water;
import wge3.entity.ground.WoodenFloor;
import wge3.entity.object.BrickWall;
import wge3.entity.object.LightSource;
import wge3.interfaces.Drawable;

public final class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private boolean needsToBeDrawn;
    private List<Tile> allTiles;
    private List<Tile> tilesToDraw;
    private List<Creature> creatures;
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<LightSource> lightSources;

    public Area() throws FileNotFoundException {
        size = 31;
        map = new Tile[size][size];
        allTiles = new LinkedList<Tile>();
        tilesToDraw = new LinkedList<Tile>();
        creatures = new LinkedList<Creature>();
        players = new LinkedList<Player>();
        NPCs = new LinkedList<NonPlayer>();
        lightSources = new LinkedList<LightSource>();
        
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
                newtile.setPosition(j, size-1 - i);
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
                    case 'L': object = new LightSource(); break;
                    default: object = null; break;
                }
                
                if (object != null) {
                    map[j][size-1 - i].setObject(object);
                }
            }
        }
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                MapObject currentObject = map[j][size-1 - i].getObject();
                if (currentObject != null && currentObject.getClass() == LightSource.class) {
                    addLightSource((LightSource) currentObject);
                    System.out.println("light source added");
                }
            }
        }
        
        mapLoader.close();
        calculateLighting();
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
    
    public void calculateFOV() {
        for (Player player : players) {
            for (Tile tile : allTiles) {
                if (tile.canBeSeenBy(player)) {
                    tilesToDraw.add(tile);
                }
            }
        }
    }
    
    public void calculateLighting() {
        for (LightSource source : lightSources) {
            int x = source.getX();
            int y = source.getY();
            int range = source.getRange();
            for (Tile tile : allTiles) {
                Color color = new Color(source.getColor());
                float dx = x - tile.getX();
                float dy = y - tile.getY();
                float distance = (float) Math.sqrt(dx*dx + dy*dy);
                if (distance <= range) {
                    float multiplier = 1f-(distance-1)*(1f/range);
                    color.mul(multiplier, multiplier, multiplier, 1f);
                    tile.setLighting(color);
                } else {
                    tile.setLighting(new Color(0, 0, 0, 1));
                }
            }
        }
    }
    
    public void addCreature(Creature guy) {
        guy.setArea(this);
        creatures.add(guy);
        if (guy.getClass() == Player.class) {
            players.add((Player) guy);
        } else {
            NPCs.add((NonPlayer) guy);
        }
    }
    
    public void addLightSource(LightSource light) {
        light.setArea(this);
        lightSources.add(light);
    }
}
