package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import wge3.entity.character.Bullet;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.mapobjects.LightSource;
import wge3.entity.terrainelements.Item;
import wge3.interfaces.Drawable;

public final class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private Random RNG;
    private TiledMap tiledMap;
    private MapLoader mapLoader;
    
    private List<Tile> allTiles;
    private List<Tile> tilesToDraw;
    private List<Creature> creatures;
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<LightSource> lightSources;
    private List<Item> items;
    private List<Bullet> bullets;
    
    private long timeOfLastPassTime;

    public Area() {
        size = 31;
        map = new Tile[size][size];
        RNG = new Random();
        
        allTiles     = new LinkedList<Tile>();
        tilesToDraw  = new LinkedList<Tile>();
        creatures    = new LinkedList<Creature>();
        players      = new LinkedList<Player>();
        NPCs         = new LinkedList<NonPlayer>();
        lightSources = new LinkedList<LightSource>();
        items        = new LinkedList<Item>();
        bullets      = new LinkedList<Bullet>();
        
        mapLoader = new MapLoader();
        try {
            mapLoader.loadMap("1", this);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Tile> getTiles() {
        return allTiles;
    }
    
    public void addTile(Tile tile, int x, int y) {
        tile.setArea(this);
        tile.setPosition(x, y);
        allTiles.add(tile);
        map[x][y] = tile;
    }

    public int getSize() {
        return size;
    }
    
    @Override
    public void draw(Batch batch) {
        drawTiles(batch);
        drawBullets(batch);
        drawCreatures(batch);
    }

    public void drawCreatures(Batch batch) {
        batch.enableBlending();
        for (Creature creature : creatures) {
            creature.draw(batch);
        }
        batch.disableBlending();
    }

    public void drawBullets(Batch batch) {
        batch.enableBlending();
        for (Bullet bullet : bullets) {
            if (bullet.exists()) {
                bullet.draw(batch);
            } else {
                removeBullet(bullet);
            }
        }
        batch.disableBlending();
    }

    public void drawTiles(Batch batch) {
        for (Iterator<Tile> it = tilesToDraw.iterator(); it.hasNext();) {
            it.next().draw(batch);
            it.remove();
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
    
    public Tile getTileAt(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        } else {
            return map[x][y];
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
        for (Player player : players) {
            float x = player.getX();
            float y = player.getY();
            int range = 12;
            for (Tile tile : allTiles) {
                if (tile.canBeSeenBy(player)) {
                    Color color = new Color(1,1,1,1);
                    float distance = tile.getDistanceTo(x, y) / Tile.size;
                    if (distance <= range) {
                        float multiplier = 1f - Math.max(distance-1, 0) * (1f/range);
                        color.mul(multiplier, multiplier, multiplier, 1f);
                        tile.setLighting(color);
                    } else {
                        tile.setLighting(new Color(0, 0, 0, 1));
                    }
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
        guy.updateSpritePosition();
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

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public void addBullet(Bullet bullet) {
        bullet.setArea(this);
        bullets.add(bullet);
    }
    
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    public void passTime(float delta) {
        long currentTime = TimeUtils.millis();
        if (currentTime - timeOfLastPassTime > 100) {
            for (Creature creature : creatures) {
                Tile tileUnderCreature = getTileAt(creature.getX(), creature.getY());
                if (tileUnderCreature.drainsHP()) {
                    creature.dealDamage((int) (tileUnderCreature.getHPDrainAmount()));
                }
                creature.regenerateHP(currentTime);
            }
            timeOfLastPassTime = currentTime;
        }
    }
}
