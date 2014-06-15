package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.ground.Grass;
import wge3.entity.ground.Water;
import wge3.entity.ground.WoodenFloor;
import wge3.entity.item.Bomb;
import wge3.entity.mapobject.BrickWall;
import wge3.entity.mapobject.LightSource;
import wge3.entity.terrainelement.Ground;
import wge3.entity.terrainelement.Item;
import wge3.entity.terrainelement.MapObject;
import wge3.interfaces.Drawable;

public final class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private boolean needsToBeDrawn;
    private Random RNG;
    private TiledMap tiledMap;
    
    private List<Tile> allTiles;
    private List<Tile> tilesToDraw;
    private List<Creature> creatures;
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<LightSource> lightSources;
    private List<Item> items;

    public Area() throws FileNotFoundException {
        size = 31;
        map = new Tile[size][size];
        RNG = new Random();
        
        allTiles = new LinkedList<Tile>();
        tilesToDraw = new LinkedList<Tile>();
        creatures = new LinkedList<Creature>();
        players = new LinkedList<Player>();
        NPCs = new LinkedList<NonPlayer>();
        lightSources = new LinkedList<LightSource>();
        items = new LinkedList<Item>();
        
        // Generate map:
        Scanner mapLoader = new Scanner(new File("maps/Untitled.tmx"));
        mapLoader.useDelimiter(",?\n?");
        for (int i = 0; i < 7; i++) mapLoader.nextLine();

        // Create tiles and load grounds:
        for (int y = size-1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
                Ground ground;
                switch (mapLoader.nextInt()) {
                    case 1: ground = new Grass(); break;
                    case 2: ground = new WoodenFloor(); break;
                    case 3: ground = new Water(); break;
                    default:ground = new Grass(); break;
                }
                Tile newtile = new Tile();
                newtile.setGround(ground);
                newtile.setPosition(x, y);
                allTiles.add(newtile);
                map[x][y] = newtile;
            }
        }
        for (int i = 0; i < 5; i++) mapLoader.nextLine();
        
        // Load objects:
        for (int y = size-1; y >= 0; y--) {
            for (int x = 0; x < size; x++) {
                MapObject object;
                switch (mapLoader.nextInt()) {
                    case 0: object = null; break;
                    case 9: object = new BrickWall(); break;
                    default:object = null; break;
                }
                if (object != null) {
                    map[x][y].setObject(object);
                }
            }
        }
        mapLoader.close();
        
        // Place 9 bombs in random locations on the map:
        for (int i = 0; i < 9; i++) {
            addItem(new Bomb());
        }

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
            if (!player.canSeeEverything()) {
                for (Tile tile : allTiles) {
                    if (tile.canBeSeenBy(player)) {
                        tilesToDraw.add(tile);
                    }
                }
            } else {
                for (Tile tile : allTiles) {
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
        // Places creature to a random tile that has no object in it.
        // If every tile has an object, this will loop infinitely.
        Tile dest;
        do {
            dest = map[RNG.nextInt(size)][RNG.nextInt(size)];
        } while (dest.hasObject());
        addCreature(guy, dest.getX(), dest.getY());
    }
    
    public void addCreature(Creature guy, int x, int y) {
        guy.setArea(this);
        guy.setPosition(x*Tile.size+Tile.size/2, y*Tile.size+Tile.size/2);
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
    
    public void addItem(Item item, int x, int y) {
        items.add(item);
        map[x][y].setObject(item);
    }
    
    public void addItem(Item item) {
        // Adds item to a random tile that has no object yet.
        // If every tile has an object, this will loop infinitely.
        Tile dest;
        do {
            dest = map[RNG.nextInt(size)][RNG.nextInt(size)];
        } while (dest.hasObject());
        addItem(item, dest.getX(), dest.getY());
    }
}
