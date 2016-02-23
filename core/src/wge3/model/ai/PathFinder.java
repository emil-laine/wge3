/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package wge3.model.ai;

import java.util.*;
import wge3.model.Area;
import wge3.model.Tile;

/**
 *
 * @author chang
 */
public class PathFinder {

    public static List<Tile> findPath(Tile start, Tile dest) {
        Queue<Tile> queue = new ArrayDeque();
        Area area = Tile.getArea();
        boolean[][] visited = new boolean[area.getWidth()][area.getHeight()];
        int[][] dist = new int[area.getWidth()][area.getHeight()];
        dist[start.getX()][start.getY()] = 0;

        HashMap<Tile, Tile> parents = new HashMap();

        queue.add(start);
        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            if (visited[current.getX()][current.getY()]) {
                continue;
            }
            visited[current.getX()][current.getY()] = true;

            parents.put(current, current.getPrevious());
            List<Tile> adjacent = current.getNearbyTiles(false);
            for (Tile tile : adjacent) {
                if (tile.isGoodMoveDest()) {
                    tile.setPrevious(current);
                    dist[tile.getX()][tile.getY()] = dist[current.getX()][current.getY()] + 1;
                    queue.add(tile);
                }
            }
        }
        return tracePathBack(parents, dest, start);
    }

    private static List<Tile> tracePathBack(HashMap<Tile, Tile> parents, Tile dest, Tile start) {
        Tile current = dest;
        List<Tile> tiles = new ArrayList();
        while (current != null) {
            if (current.getX() == start.getX() && current.getY() == start.getY()) {
                break;
            }
            tiles.add(current);
            current = parents.get(current);
        }

        Collections.reverse(tiles);
        return calculateWaypoints(tiles);
    }

    private static List<Tile> calculateWaypoints(List<Tile> route) {
        List<Tile> waypoints = new ArrayList();

        Tile current = route.get(0);
        for (int i = 1; i < route.size(); i++) {
            Tile next = route.get(i);

            if (!next.isAnOKMoveDestinationFrom(current.getMiddleX(), current.getMiddleY())) {
                // add the previous tile
                waypoints.add(route.get(i - 1));
                current = route.get(i - 1);
            }
        }

        waypoints.add(route.get(route.size() - 1));

        return waypoints;
    }
}
