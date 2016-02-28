/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.randomBoolean;
import static com.badlogic.gdx.math.MathUtils.sin;
import com.badlogic.gdx.math.Rectangle;
import static com.badlogic.gdx.utils.TimeUtils.millis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import wge3.engine.util.Drawable;
import static wge3.engine.util.Math.floatPosToTilePos;
import wge3.model.actors.Player;
import wge3.model.items.Bomb;
import wge3.model.objects.GreenSlime;
import wge3.model.objects.Item;
import wge3.model.objects.Tree;

public final class Area implements Drawable {
    private Tile[][] tiles;
    private int width;
    private int height;
    private Rectangle bounds;
    
    private List<Tile> allTiles;
    private List<Entity> entities;
    
    private List<Player> players;
    private List<NonPlayer> NPCs;
    private List<Item> items;
    private List<GreenSlime> slimes;
    
    private List<Tile> tilesToDraw;
    private List<Tree> treesToDraw;
    
    private long timeOfLastPassTime;
    
    public Area(String mapName) {
        allTiles     = new ArrayList();
        entities     = new ArrayList();
        
        players      = new ArrayList();
        NPCs         = new ArrayList();
        items        = new ArrayList();
        slimes       = new ArrayList();
        
        tilesToDraw  = new LinkedList();
        treesToDraw  = new LinkedList();
        
        loadMap(mapName);
    }
    
    /** Initializes this Area from the given map file. */
    public void loadMap(String mapFileName) {
        try {
            MapLoader.loadMap(mapFileName, this);
        } catch (IOException ex) {
            Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Returns every Tile that belongs to this Area. */
    public List<Tile> getTiles() {
        return allTiles;
    }
    
    /** Initializes the Tile matrix. */
    public void createTiles(int width, int height) {
        this.width = width;
        this.height = height;
        bounds = new Rectangle(0, 0, width * Tile.size, height * Tile.size);
        tiles = new Tile[width][height];
    }
    
    /** Adds the given Tile to the given position in this map. */
    public void addTile(Tile tile, int x, int y) {
        tile.setPosition(x, y);
        allTiles.add(tile);
        tiles[x][y] = tile;
    }
    
    /** Returns the number of column this Area has. */
    public int getWidth() {
        return width;
    }
    
    /** Returns the number of rows this Area has. */
    public int getHeight() {
        return height;
    }
    
    /** Returns the rectangular region that this Area occupies. */
    public Rectangle getBounds() {
        return bounds;
    }
    
    @Override
    public void draw(Batch batch) {
        drawTiles(batch);
        batch.enableBlending();
        drawEntities(batch);
        drawTrees(batch);
        batch.disableBlending();
    }
    
    /** Draws all tiles that should be redrawn.
     *  @param batch the libGDX batch object that handles all drawing. */
    public void drawTiles(Batch batch) {
        tilesToDraw.stream().forEach(tile -> tile.draw(batch));
        tilesToDraw.clear();
    }
    
    /** Checks whether the given position is a valid location on this map. */
    public boolean hasLocation(float x, float y) {
        return hasLocation(floatPosToTilePos(x), floatPosToTilePos(y));
    }
    
    /** Checks whether the given tile grid position is valid on this map. */
    public boolean hasLocation(int x, int y) {
        return x >= 0
            && x < this.getWidth()
            && y >= 0
            && y < this.getHeight();
    }
    
    /** Returns the Tile that's under the given game world position (x, y).
     *  This method won't return null, so the coordinates must be valid. */
    public Tile getTileAt(float x, float y) {
        return getTileAt(floatPosToTilePos(x), floatPosToTilePos(y));
    }
    
    /** Returns the Tile at position (x, y) in the tile grid. The parameters
     *  must be valid coordinates. */
    public Tile getTileAt(int x, int y) {
        return tiles[x][y];
    }
    
    /** Returns the Tiles whose bounds overlap the given region. */
    public List<Tile> getOverlappingTiles(Rectangle region) {
        final int minX = floatPosToTilePos(region.x);
        final int maxX = Math.min(width-1, floatPosToTilePos(region.x + region.width));
        final int minY = floatPosToTilePos(region.y);
        final int maxY = Math.min(height-1, floatPosToTilePos(region.y + region.height));
        
        List<Tile> results = new ArrayList((maxX - minX + 1) * (maxY - minY + 1));
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                results.add(getTileAt(x, y));
            }
        }
        
        return results;
    }
    
    /** Specifies a Tile that should be redrawn. */
    public void addTileToDraw(Tile tile) {
        tilesToDraw.add(tile);
    }
    
    /** Draws all entities that can be seen by a player. */
    public void drawEntities(Batch batch) {
        for (Creature player : players) {
            for (Entity entity : entities) {
                if (entity.canBeSeenBy(player)) {
                    entity.draw(batch);
                }
            }
        }
    }
    
    /** Requests redraw on Tiles that are currently visible to any players. */
    public void calculateFOV() {
        players.stream().forEach((player) -> {
            if (!player.seesEverything()) {
                allTiles.stream()
                        .filter((tile) -> tile.canBeSeenBy(player))
                        .forEach((tile) -> tile.requestDraw());
            } else {
                allTiles.stream()
                        .forEach((tile) -> tile.requestDraw());
            }
        });
    }
    
    /** Sets the lightning for all visible Tiles according to how close they're
     *  to any players. */
    public void calculateLighting() {
        for (Player player : players) {
            if (player.seesEverything())
                continue;
            
            final float x = player.getX();
            final float y = player.getY();
            final int range = player.getSight();
            allTiles.stream()
                    .filter((tile) -> (tile.canBeSeenBy(player)))
                    .forEach((tile) -> {
                Color color = new Color(1, 1, 1, 1);
                final float distance = tile.getDistanceTo(x, y) / Tile.size;
                float multiplier = 1f - Math.max(distance-1, 0) * (1f/range);
                multiplier = getTilesOnLine(x, y, tile.getMiddleX(), tile.getMiddleY())
                        .stream()
                        .filter((tile2) -> tile2.castsShadows())
                        .map((tile2) -> tile2.getObject().getShadowDepth())
                        .reduce(multiplier, (accumulator, _item) -> accumulator * _item);
                color.mul(multiplier, multiplier, multiplier, 1f);
                tile.setLighting(color);
            });
        }
    }
    
    /** Places the given Creature to a random Tile in this Area that has no
     *  object on it. If every Tile has an object, this will loop infinitely. */
    public void addCreature(Creature guy) {
        Tile dest;
        do {
            dest = tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
        } while (dest.hasObject());
        addCreature(guy, dest.getX(), dest.getY());
    }
    
    /** Places the given Creature to the Tile in the specified position. */
    public void addCreature(Creature guy, int x, int y) {
        guy.setArea(this);
        guy.setPosition(x, y);
        guy.updateSpritePosition();
        entities.add(guy);
        if (guy.getClass() == Player.class) {
            players.add((Player) guy);
        } else {
            NPCs.add((NonPlayer) guy);
        }
    }
    
    /** Deletes the given Creature from this Area. */
    public void removeCreature(Creature creature) {
        entities.remove(creature);
        if (!creature.isPlayer()) {
            NPCs.remove((NonPlayer) creature);
        } else {
            players.remove((Player) creature);
        }
    }
    
    /** Places the given Item to the Tile in the specified position. */
    public void addItem(Item item, int x, int y) {
        items.add(item);
        tiles[x][y].setObject(item);
    }
    
    /** Places the given Item to a random Tile in this Area that has no object
     *  on it. If every Tile has an object, this will loop infinitely. */
    public void addItem(Item item) {
        Tile dest;
        do {
            dest = tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
        } while (dest.hasObject());
        addItem(item, dest.getX(), dest.getY());
    }
    
    /** Returns all Creatures that are currently in this Area. */
    // TODO: Remove this method.
    @Deprecated
    public List<Creature> getCreatures() {
        return getListOfEntitiesOfType(Creature.class);
    }
    
    /** Returns all Players that are currently in this Area. */
    public List<Player> getPlayers() {
        return players;
    }
    
    /** Returns all NonPlayers that are currently in this Area. */
    public List<NonPlayer> getNPCs() {
        return NPCs;
    }
    
    /** Returns all Bombs that are currently in this Area. */
    // TODO: Remove this method.
    @Deprecated
    public List<Bomb> getBombs() {
        return getListOfEntitiesOfType(Bomb.class);
    }
    
    /** Considers the given Bomb to belong to this Area. */
    // TODO: Replace with addEntity(Entity).
    @Deprecated
    public void addBomb(Bomb bomb) {
        bomb.setArea(this);
        entities.add(bomb);
    }
    
    /** Considers the given Bomb to no more belong to this Area. */
    // TODO: Replace with removeEntity(Entity)
    @Deprecated
    public void removeBomb(Bomb bomb) {
        entities.remove(bomb);
    }
    
    /** Updates everything in this Area that is affected by time. For example,
     *  the expansion of slimes, and interactive terrains such as lava. */
    public void passTime(float delta) {
        long currentTime = millis();
        if (currentTime - timeOfLastPassTime > 100) {
            getCreatures().stream().map((creature) -> {
                Tile tileUnderCreature = creature.getTileUnder();
                if (tileUnderCreature.drainsHP() && !creature.isGhost() && !creature.isFlying()) {
                    creature.dealDamage(tileUnderCreature.getHPDrainAmount());
                }
                return creature;
            }).forEach((creature) -> {
                creature.regenerate(currentTime);
            });
            timeOfLastPassTime = currentTime;
            
            expandSlimes();
        }
    }
    
    /** Returns all Tiles that the specified line segment with endpoints
     *  (startX, startY) and (finalX, finalY) intersects. The coordinates must
     *  refer to valid locations on this map. */
    public List<Tile> getTilesOnLine(float startX, float startY, float finalX, float finalY) {
        assert this.hasLocation(startX, startY) && this.hasLocation(finalX, finalY)
            : "Illegal arguments passed to getTilesOnLine()";
  
        int startTileX = floatPosToTilePos(startX);
        int startTileY = floatPosToTilePos(startY);
        int finalTileX = floatPosToTilePos(finalX);
        int finalTileY = floatPosToTilePos(finalY);
        
        if (startTileX == finalTileX && startTileY == finalTileY)
            return Collections.EMPTY_LIST;
        
        float angle = atan2(finalY - startY, finalX - startX);
        float xUnit = cos(angle);
        float yUnit = sin(angle);
        
        int i = 0;
        int currentTileX;
        int currentTileY;
        int previousTileX = startTileX;
        int previousTileY = startTileY;
        
        List<Tile> tilesOnLine = new ArrayList();
        
        while (true) {
            do {
                i++;
                currentTileX = floatPosToTilePos(startX + i * xUnit);
                currentTileY = floatPosToTilePos(startY + i * yUnit);
            } while (currentTileX == previousTileX && currentTileY == previousTileY);
            
            if (currentTileX == finalTileX && currentTileY == finalTileY) break;
            
            tilesOnLine.add(getTileAt(currentTileX, currentTileY));
            
            previousTileX = currentTileX;
            previousTileY = currentTileY;
        }
        
        return tilesOnLine;
    }
    
    /** Gets rid of all external resources associated with this Area to avoid
     *  memory leaks. */
    public void dispose() {
        getBombs().stream().forEach((bomb) -> {
            bomb.cancelTimer();
        });
        // ...
    }
    
    /** Specifies a Tree that should be redrawn. */
    public void addTreeToDraw(Tree tree) {
        treesToDraw.add(tree);
    }
    
    /** Draws all Trees in this Area.
     *  @param batch the libGDX batch object that handles all drawing. */
    public void drawTrees(Batch batch) {
        batch.enableBlending();
        for (Iterator<Tree> it = treesToDraw.iterator(); it.hasNext();) {
            Tree tree = it.next();
            tree.draw(batch);
            it.remove();
        }
        batch.disableBlending();
    }
    
    /** Considers the given GreenSlime to belong to this Area. */
    public void addSlime(GreenSlime slime) {
        slimes.add(slime);
    }
    
    /** Causes all slimes in this Area to multiply according to their expansion
     *  rate. */
    public void expandSlimes() {
        List<GreenSlime> newSlimes = new ArrayList();
        
        slimes.stream()
                .filter((slime) -> (randomBoolean(GreenSlime.expansionProbability)))
                .map((slime) -> slime.expand())
                .filter((newSlime) -> (newSlime != null))
                .forEach((newSlime) -> newSlimes.add(newSlime));
        
        slimes.addAll(newSlimes);
    }
    
    /** Returns a randomly selected Tile in this Area. */
    public Tile getRandomTile() {
        return tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
    }
    
    public Tile getRandomTileWithoutObject() {
        Tile dest;
        do {
            dest = tiles[MathUtils.random(width-1)][MathUtils.random(height-1)];
        } while (dest.hasObject());
        return dest;
    }
    
    private <T extends Entity> List<T> getListOfEntitiesOfType(Class<T> type) {
        return entities.stream()
                .filter(e -> type.isAssignableFrom(e.getClass()))
                .map(e -> (T) e)
                .collect(Collectors.toList());
    }
}
