package wge3.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.sin;
import com.badlogic.gdx.utils.TimeUtils;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wge3.entity.character.Bomb;
import wge3.entity.character.Creature;
import wge3.entity.character.NonPlayer;
import wge3.entity.character.Player;
import wge3.entity.mapobjects.GreenSlime;
import wge3.entity.mapobjects.LightSource;
import wge3.entity.terrainelements.Item;
import wge3.game.Drawable;

public final class Area implements Drawable {
    private Tile[][] map;
    private int size;
    private TiledMap tiledMap;
    private MapLoader mapLoader;
    
    private List<Tile> allTiles;
    private List<Tile> tilesToDraw;
    private List<Creature> creatures;
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<LightSource> lightSources;
    private List<Item> items;
    private List<Bomb> bombs;
    
    private long timeOfLastPassTime;

    public Area(String mapName) {
        size = 31;
        map = new Tile[size][size];
        
        allTiles     = new LinkedList<Tile>();
        tilesToDraw  = new LinkedList<Tile>();
        creatures    = new LinkedList<Creature>();
        players      = new LinkedList<Player>();
        NPCs         = new LinkedList<NonPlayer>();
        lightSources = new LinkedList<LightSource>();
        items        = new LinkedList<Item>();
        bombs        = new LinkedList<Bomb>();
        
        mapLoader = new MapLoader();
        try {
            mapLoader.loadMap(mapName, this);
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
        drawBombs(batch);
        drawCreatures(batch);
    }

    public void drawTiles(Batch batch) {
        for (Iterator<Tile> it = tilesToDraw.iterator(); it.hasNext();) {
            it.next().draw(batch);
            it.remove();
        }
    }
    
    public boolean hasLocation(float x, float y) {
        return hasLocation(floatPosToTilePos(x), floatPosToTilePos(y));
    }
    
    public boolean hasLocation(int x, int y) {
        return x >= 0
            && x < this.getSize()
            && y >= 0
            && y < this.getSize();
    }
    
    public Tile getTileAt(float x, float y) {
        return getTileAt(floatPosToTilePos(x), floatPosToTilePos(y));
    }
    
    public Tile getTileAt(int x, int y) {
        if (!hasLocation(x, y)) throw new IllegalArgumentException();
        return map[x][y];
    }
    
    public void addToDrawList(Tile tile) {
        tilesToDraw.add(tile);
    }
    
    public void drawCreatures(Batch batch) {
        batch.enableBlending();
        for (Player player : players) {
            for (Creature creature : NPCs) {
                if (creature.canBeSeenBy(player)) {
                    creature.draw(batch);
                }
            }
            player.draw(batch);
        }
        batch.disableBlending();
    }

    public void drawBombs(Batch batch) {
        batch.enableBlending();
        for (Player player : players) {
            for (Bomb bomb : bombs) {
                if (bomb.canBeSeenBy(player)) {
                    bomb.draw(batch);
                }
            }
        }
        batch.disableBlending();
    }

    public void calculateFOV() {
        for (Player player : players) {
            if (!player.canSeeEverything()) {
                for (Tile tile : allTiles) {
                    if (tile.canBeSeenBy(player)) {
                        tile.requestDraw();
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
            if (player.canSeeEverything()) {
                Color color = new Color(1, 1, 1, 1);
                for (Tile tile : allTiles) {
                    tile.setLighting(color);
                }
            } else {
                float x = player.getX();
                float y = player.getY();
                int range = player.getSight();
                for (Tile tile : allTiles) {
                    if (tile.canBeSeenBy(player)) {
                        Color color = new Color(1, 1, 1, 1);
                        float distance = tile.getDistanceTo(x, y) / Tile.size;
                        float multiplier = 1f - Math.max(distance-1, 0) * (1f/range);
                        color.mul(multiplier, multiplier, multiplier, 1f);
                        tile.setLighting(color);
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
            dest = map[MathUtils.random(size-1)][MathUtils.random(size-1)];
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
    
    public void removeCreature(Creature creature) {
        creatures.remove(creature);
        if (!creature.isPlayer()) {
            NPCs.remove((NonPlayer) creature);
        } else {
            players.remove((Player) creature);
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
            dest = map[MathUtils.random(size-1)][MathUtils.random(size-1)];
        } while (dest.hasObject());
        addItem(item, dest.getX(), dest.getY());
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<NonPlayer> getNPCs() {
        return NPCs;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }
    
    public void addBomb(Bomb bomb) {
        bomb.setArea(this);
        bombs.add(bomb);
    }
    
    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
    }

    public void passTime(float delta) {
        long currentTime = TimeUtils.millis();
        if (currentTime - timeOfLastPassTime > 100) {
            for (Creature creature : creatures) {
                Tile tileUnderCreature = getTileAt(creature.getX(), creature.getY());
                if (tileUnderCreature.drainsHP() && !creature.isGhost()) {
                    creature.dealDamage((int) (tileUnderCreature.getHPDrainAmount()));
                }
                creature.regenerateHP(currentTime);
            }
            timeOfLastPassTime = currentTime;
            
            for (Tile tile : getTiles()) {
                if (tile.hasSlime() && randomBoolean(GreenSlime.expansionProbability)) {
                    GreenSlime slime = (GreenSlime) tile.getObject();
                    slime.expand();
                }
            }
        }
    }
    
    public static int floatPosToTilePos(float pos) {
        return floor(pos)/Tile.size;
    }
    
    List<Tile> getTilesOnLine(float startX, float startY, float finalX, float finalY) {
        if (!this.hasLocation(startX, startY) || !this.hasLocation(finalY, finalY))
            throw new IllegalArgumentException();
        
        float angle = atan2(finalY-startY, finalX-startX);
        float xUnit = cos(angle);
        float yUnit = sin(angle);
        
        int startTileX = floatPosToTilePos(startX);
        int startTileY = floatPosToTilePos(startY);
        int finalTileX = floatPosToTilePos(finalX);
        int finalTileY = floatPosToTilePos(finalY);
        
        List<Tile> tiles = new LinkedList<Tile>();
        if (startTileX == finalTileX && startTileY == finalTileY) return tiles;
        
        int i = 0;
        int currentTileX;
        int currentTileY;
        int previousTileX = startTileX;
        int previousTileY = startTileY;
        
        while (true) {
            do {
                i++;
                currentTileX = floatPosToTilePos(startX + i * xUnit);
                currentTileY = floatPosToTilePos(startY + i * yUnit);
            } while (currentTileX == previousTileX && currentTileY == previousTileY);
            
            if (currentTileX == finalTileX && currentTileY == finalTileY) break;
            
            tiles.add(getTileAt(currentTileX, currentTileY));

            previousTileX = currentTileX;
            previousTileY = currentTileY;
        }
        
        return tiles;
    }
    
    public void dispose() {
        for (Bomb bomb : getBombs()) {
            bomb.cancelTimer();
        }
    }
}
