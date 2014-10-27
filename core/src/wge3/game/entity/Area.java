package wge3.game.entity;

import wge3.game.engine.utilities.MapLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.sin;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wge3.game.entity.bombs.Bomb;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.tilelayers.mapobjects.GreenSlime;
import wge3.game.entity.tilelayers.mapobjects.Tree;
import wge3.game.entity.tilelayers.mapobjects.Item;
import wge3.game.engine.gui.Drawable;
import static wge3.game.engine.utilities.Math.floatPosToTilePos;

public final class Area implements Drawable {
    private Tile[][] tiles;
    private int width;
    private int height;
    
    private List<Tile> allTiles;
    private List<Creature> creatures;
    private List<Creature> flyingCreatures;
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<Item> items;
    private List<Bomb> bombs;
    private List<GreenSlime> slimes;
    
    private List<Tile> tilesToDraw;
    private List<Tree> treesToDraw;
    
    private long timeOfLastPassTime;

    public Area(String mapName) {
        allTiles     = new ArrayList<Tile>();
        creatures    = new ArrayList<Creature>();
        flyingCreatures = new ArrayList<Creature>();
        players      = new ArrayList<Player>();
        NPCs         = new ArrayList<NonPlayer>();
        items        = new ArrayList<Item>();
        bombs        = new ArrayList<Bomb>();
        slimes       = new ArrayList<GreenSlime>();
        
        tilesToDraw  = new LinkedList<Tile>();
        treesToDraw  = new LinkedList<Tree>();
        
        loadMap(mapName);
    }
    
    public void loadMap(String mapFileName) {
        try {
            new MapLoader().loadMap(mapFileName, this);
        } catch (IOException ex) {
            Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Tile> getTiles() {
        return allTiles;
    }
    
    public void createTiles(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
    }
    
    public void addTile(Tile tile, int x, int y) {
        tile.setArea(this);
        tile.setPosition(x, y);
        allTiles.add(tile);
        tiles[x][y] = tile;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(Batch batch) {
        drawTiles(batch);
        batch.enableBlending();
        drawBombs(batch);
        drawNonFlyingCreatures(batch);
        drawTrees(batch);
        drawFlyingCreatures(batch);
        batch.disableBlending();
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
            && x < this.getWidth()
            && y >= 0
            && y < this.getHeight();
    }
    
    public Tile getTileAt(float x, float y) {
        return getTileAt(floatPosToTilePos(x), floatPosToTilePos(y));
    }
    
    public Tile getTileAt(int x, int y) {
        return tiles[x][y];
    }
    
    public void addTileToDraw(Tile tile) {
        tilesToDraw.add(tile);
    }
    
    public void drawNonFlyingCreatures(Batch batch) {
        for (Creature player : players) {
            for (Creature NPC : NPCs) {
                if ((NPC.canBeSeenBy(player))) {
                    if (!NPC.isFlying()) NPC.draw(batch);
                    else flyingCreatures.add(NPC);
                }
            }
            if (!player.isFlying()) player.draw(batch);
            else flyingCreatures.add(player);
        }
    }
    
    public void drawFlyingCreatures(Batch batch) {
        for (Creature creature : flyingCreatures) {
            creature.draw(batch);
        }
    }

    public void drawBombs(Batch batch) {
        for (Player player : players) {
            for (Bomb bomb : bombs) {
                if (bomb.canBeSeenBy(player)) {
                    bomb.draw(batch);
                }
            }
        }
    }

    public void calculateFOV() {
        for (Player player : players) {
            if (!player.seesEverything()) {
                for (Tile tile : allTiles) {
                    if (tile.canBeSeenBy(player)) {
                        tile.requestDraw();
                    }
                }
            } else {
                for (Tile tile : allTiles) {
                    tile.requestDraw();
                }
            }
        }
    }
    
    public void calculateLighting() {
        for (Player player : players) {
            if (player.seesEverything()) {
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
                        for (Tile tile2 : getTilesOnLine(x, y, tile.getMiddleX(), tile.getMiddleY()))
                            if (tile2.castsShadows()) multiplier *= tile2.getObject().getShadowDepth();
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
            dest = tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
        } while (dest.hasObject());
        addCreature(guy, dest.getX(), dest.getY());
    }
    
    public void addCreature(Creature guy, int x, int y) {
        guy.setArea(this);
        guy.setPosition(x, y);
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
    
    public void addItem(Item item, int x, int y) {
        items.add(item);
        tiles[x][y].setObject(item);
    }
    
    public void addItem(Item item) {
        // Adds item to a random tile that has no object yet.
        // If every tile has an object, this will loop infinitely.
        Tile dest;
        do {
            dest = tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
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
        long currentTime = millis();
        if (currentTime - timeOfLastPassTime > 100) {
            for (Creature creature : creatures) {
                Tile tileUnderCreature = creature.getTile();
                if (tileUnderCreature.drainsHP() && !creature.isGhost() && !creature.isFlying()) {
                    creature.dealDamage(tileUnderCreature.getHPDrainAmount());
                }
                creature.regenerate(currentTime);
            }
            timeOfLastPassTime = currentTime;
            
            expandSlimes();
        }
    }
    
    public List<Tile> getTilesOnLine(float startX, float startY, float finalX, float finalY) {
        if (!this.hasLocation(startX, startY) || !this.hasLocation(finalY, finalY))
            throw new IllegalArgumentException();
        
        float angle = atan2(finalY-startY, finalX-startX);
        float xUnit = cos(angle);
        float yUnit = sin(angle);
  
        int startTileX = floatPosToTilePos(startX);
        int startTileY = floatPosToTilePos(startY);
        int finalTileX = floatPosToTilePos(finalX);
        int finalTileY = floatPosToTilePos(finalY);
        
        List<Tile> tiles = new ArrayList<Tile>();
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
        // ...
    }
    
    public void addTreeToDraw(Tree tree) {
        treesToDraw.add(tree);
    }
    
    public void drawTrees(Batch batch) {
        batch.enableBlending();
        for (Iterator<Tree> it = treesToDraw.iterator(); it.hasNext();) {
            Tree tree = it.next();
            tree.draw(batch);
            it.remove();
        }
        batch.disableBlending();
    }
    
    public void addSlime(GreenSlime slime) {
        slimes.add(slime);
    }
    
    public void expandSlimes() {
        List<GreenSlime> newSlimes = new ArrayList<GreenSlime>();
        
        for (GreenSlime slime : slimes) {
            if (randomBoolean(GreenSlime.expansionProbability)) {
                GreenSlime newSlime = slime.expand();
                if (newSlime != null)
                    newSlimes.add(newSlime);
            }
        }
        
        slimes.addAll(newSlimes);
    }
    
    public Tile getRandomTile() {
        return tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
    }
    
    public List<Creature> getCreaturesNear(float x, float y) {
        List<Creature> creatures = new ArrayList<Creature>();
        for (Tile tile : getTileAt(x, y).getNearbyTiles(true)) {
            creatures.addAll(tile.getCreatures());
        }
        return creatures;
    }
}
