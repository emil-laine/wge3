/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static java.lang.Math.sqrt;
import wge3.model.Tile;
import wge3.model.Creature;

public final class Math {
    
    /** Calculates the angle between the points (x1, y1) and (x2, y2). */
    public static float angle(float x1, float y1, float x2, float y2) {
        float angle = atan2(y2 - y1, x2 - x1);
        if (angle < 0) return angle + PI2;
        return angle;
    }
    
    /** Calculates the difference between the angles src and dst.
     *  @return in the range [-PI, +PI] */
    public static float getDiff(float src, float dst) {
        float diff = dst - src;
        if (diff > PI) return diff - PI2;
        if (diff < -PI) return diff + PI2;
        return diff;
    }
    
    /** Returns the tile grid coordinate of the tile that the game world
     *  coordinate pos belongs in. */
    public static int floatPosToTilePos(float pos) {
        return ((int) pos) / Tile.size;
    }
    
    /** Checks whether the Creature c is in the middle of a tile. */
    public static boolean isInCenterOfATile(Creature c) {
        return isInCenterOfATile(c.getX(), c.getY());
    }
    
    /** Checks whether the specified point is in the middle of a tile. */
    public static boolean isInCenterOfATile(float x, float y) {
        return isInCenterOfATile(x, y, 0.25f);
    }
    
    /** Checks whether the specified point is in the middle of a tile.
     *  @param border where the part considered "middle" starts */
    public static boolean isInCenterOfATile(float x, float y, float border) {
        x = (x % Tile.size) / Tile.size;
        y = (y % Tile.size) / Tile.size;
        float opposite = 1f - border;
        return (x >= border && x < opposite) && (y >= border && y < opposite);
    }
    
    /** Calculates the distance from point (x1, y1) to point (x2, y2). */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) sqrt(getDistance2(x1, y1, x2, y2));
    }
    
    /** Calculates the squared distance from (x1, y1) to (x2, y2). */
    public static float getDistance2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (dx * dx) + (dy * dy);
    }
    
    /** Calculates the distance from Creature c to point (x, y). */
    public static float getDistance(Creature c, float x, float y) {
        return getDistance(c.getX(), c.getY(), x, y);
    }
    
    /** Calculates the squared distance from Creature c to point (x, y). */
    public static float getDistance2(Creature c, float x, float y) {
        return getDistance2(c.getX(), c.getY(), x, y);
    }
    
    /** Calculates the distance from Creature c1 to Creature c2. */
    public static float getDistance(Creature c1, Creature c2) {
        return getDistance(c1.getX(), c1.getY(), c2.getX(), c2.getY());
    }
    
    /** Calculates the squared distance from Creature c1 to Creature c2. */
    public static float getDistance2(Creature c1, Creature c2) {
        return getDistance2(c1.getX(), c1.getY(), c2.getX(), c2.getY());
    }
    
    /** Calculates the distance from point (x1, y1) to point (x2, y2) in tiles.
     *  @return distance from (x1, y1) to (x2, y2) in tiles, rounded down */
    public static float getDistanceInTiles(float x1, float y1, float x2, float y2) {
        return getDistance(x1, y1, x2, y2) / Tile.size;
    }
    
    /** Calculates the distance from Creature c to point (x, y) in tiles.
     *  @return distance from c to (x, y) in tiles, rounded down */
    public static float getDistanceInTiles(Creature c, float x, float y) {
        return getDistanceInTiles(c.getX(), c.getY(), x, y);
    }
    
    /** Calculates the squared distance from Creature c to point (x, y) in
     *  tiles.
     *  @return squared distance from c to (x, y) in tiles, rounded down */
    public static float getDistance2InTiles(Creature c, float x, float y) {
        return getDistance2(c, x, y) / Tile.size;
    }
    
    /** Calculates the distance from Creature c to Tile tile in tiles.
     *  @return distance from c to tile in tiles, rounded down */
    public static float getDistanceInTiles(Creature c, Tile tile) {
        return getDistanceInTiles(c, tile.getMiddleX(), tile.getMiddleY());
    }
    
    /** Calculates the squared distance from Creature c to Tile tile in tiles.
     *  @return squared distance from c to tile in tiles, rounded down */
    public static float getDistance2InTiles(Creature c, Tile tile) {
        return getDistance2InTiles(c, tile.getMiddleX(), tile.getMiddleY());
    }
    
    private Math() {}
}
