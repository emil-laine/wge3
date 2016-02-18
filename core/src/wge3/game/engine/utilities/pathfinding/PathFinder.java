/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.engine.utilities.pathfinding;

import static com.badlogic.gdx.math.MathUtils.random;
import java.util.*;
import wge3.game.entity.Area;
import wge3.game.entity.Tile;
/**
 *
 * @author chang
 */

public class PathFinder {
    
    public static List<Tile> findPath(Tile start, Tile dest) {
        if (!dest.isGoodMoveDest()) {
            return null;
        }
        
        List<TileData> allTiles = calculateTileData(dest);
        List<TileData> route = calculateRoute(allTiles, start);
        if (route == null) return null;
        List<Tile> waypoints = calculateWaypoints(route, dest.getArea());
        return waypoints;
    }
    
    private static List<TileData> calculateTileData(Tile origin) {
        Area area = origin.getArea();
        int c = 0;
        List<TileData> tileData = new ArrayList();
        List<TileData> queue = new ArrayList();
        queue.add(new TileData(origin.getX(), origin.getY(), true));
        
        int dataAdded;
        
        do {
            c++;
            
            List<TileData> toAdd = getNearbyTileData(c, queue, area);
            queue.clear();
            
            // add new tiledata to queue if it isn't already there:
            dataAdded = 0;
            for (TileData newData : toAdd) {
                boolean alreadyExists = false;
                for (TileData oldData : tileData) {
                    if (oldData.getX() == newData.getX() && oldData.getY() == newData.getY()) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (!alreadyExists) {
                    tileData.add(newData);
                    queue.add(newData);
                    dataAdded++;
                }
            }
            // when there's no new data left to add:
        } while (dataAdded != 0);
        
        return tileData;
    }
    
    private static List<TileData> getNearbyTileData(int counter, List<TileData> tiles, Area area){
        List<TileData> newTiles = new ArrayList();
        
        for (TileData tile : tiles) {
            int x = tile.getX();
            int y = tile.getY();
            if (area.hasLocation(x+1, y) && area.getTileAt(x+1, y).isGoodMoveDest()) {
                newTiles.add(new TileData(x+1, y, counter));
            }
            if (area.hasLocation(x-1, y) && area.getTileAt(x-1, y).isGoodMoveDest()) {
                newTiles.add(new TileData(x-1, y, counter));
            }
            if (area.hasLocation(x, y+1) && area.getTileAt(x, y+1).isGoodMoveDest()) {
                newTiles.add(new TileData(x, y+1, counter));
            }
            if (area.hasLocation(x, y-1) && area.getTileAt(x, y-1).isGoodMoveDest()) {
                newTiles.add(new TileData(x, y-1, counter));
            }
        }
        
        return newTiles;
    }
    
    private static List<TileData> calculateRoute(List<TileData> tileData, Tile start) {
        List<TileData> route = new ArrayList();
        
        int startX = start.getX();
        int startY = start.getY();
        int c = 0;
        
        // find starting point
        for (TileData tile : tileData) {
            if (tile.getX() == startX && tile.getY() == startY) {
                c = tile.getCounter() - 1;
                route.add(tile);
            }
        }
        
        if (route.isEmpty()) return null;
        
        TileData last;
        List<TileData> alternatives = new ArrayList();
        int i = tileData.size() - 1;
        
        do {
            last = route.get(route.size() - 1);
            
            for (; i >= 0; i--) {
                TileData next = tileData.get(i);
                if (next.getCounter() < c) break;
                if (next.getCounter() > c) continue;
                if (next.isNextTo(last)) {
                    alternatives.add(next);
                }
            }
            
            if (alternatives.isEmpty()) break;
            route.add(alternatives.get(random(alternatives.size() - 1)));
            alternatives.clear();
            c--;
        } while (c > 0);
        
        route.add(tileData.get(0)); // add the last one
        
        return route;
    }
    
    private static List<Tile> calculateWaypoints(List<TileData> route, Area area) {
        List<Tile> waypoints = new ArrayList();
        
        Tile current = area.getTileAt(route.get(0).getX(), route.get(0).getY());
        
        for (int i = 1; i < route.size(); i++) {
            Tile next = area.getTileAt(route.get(i).getX(), route.get(i).getY());
            
            if (!next.isAnOKMoveDestinationFrom(current.getMiddleX(), current.getMiddleY())) {
                // add the previous tile
                waypoints.add(area.getTileAt(route.get(i-1).getX(), route.get(i-1).getY()));
                current = area.getTileAt(route.get(i-1).getX(), route.get(i-1).getY());
            }
        }
        
        waypoints.add(area.getTileAt(route.get(route.size()-1).getX(), route.get(route.size()-1).getY()));
        
        return waypoints;
    }
}
