/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import com.badlogic.gdx.math.Vector2;
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
    
    /** Returns a new vector created from the results of calling
     *  {@link #floatPosToTilePos(float) with both x and y components of
     *  the given vector. */
    public static Vector2 floatPosToTilePos(Vector2 pos) {
        return new Vector2(floatPosToTilePos(pos.x), floatPosToTilePos(pos.y));
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
    
    /** Calculates the distance between the given points. */
    public static float getDistance(Vector2 pos1, Vector2 pos2) {
        return (float) sqrt(getDistance2(pos1, pos2));
    }
    
    /** Calculates the squared distance between the given points. */
    public static float getDistance2(Vector2 pos1, Vector2 pos2) {
        return pos2.cpy().sub(pos1).len2();
    }
    
    /** Calculates the distance from Creature c to pos. */
    public static float getDistance(Creature c, Vector2 pos) {
        return getDistance(c.getPos(), pos);
    }
    
    /** Calculates the squared distance from Creature c to pos. */
    public static float getDistance2(Creature c, Vector2 pos) {
        return getDistance2(c.getPos(), pos);
    }
    
    /** Calculates the distance from Creature c1 to Creature c2. */
    public static float getDistance(Creature c1, Creature c2) {
        return getDistance(c1.getPos(), c2.getPos());
    }
    
    /** Calculates the squared distance from Creature c1 to Creature c2. */
    public static float getDistance2(Creature c1, Creature c2) {
        return getDistance2(c1.getPos(), c2.getPos());
    }
    
    /** Calculates the distance between pos1 and pos2 in tiles.
     *  @return distance from (x1, y1) to (x2, y2) in tiles, rounded down */
    public static float getDistanceInTiles(Vector2 pos1, Vector2 pos2) {
        return getDistance(pos1, pos2) / Tile.size;
    }
    
    /** Calculates the distance from Creature c to pos in tiles.
     *  @return distance from c to (x, y) in tiles, rounded down */
    public static float getDistanceInTiles(Creature c, Vector2 pos) {
        return getDistanceInTiles(c.getPos(), pos);
    }
    
    /** Calculates the squared distance from Creature c to pos in tiles.
     *  @return squared distance from c to (x, y) in tiles, rounded down */
    public static float getDistance2InTiles(Creature c, Vector2 pos) {
        return getDistance2(c, pos) / Tile.size;
    }
    
    /** Calculates the distance from Creature c to Tile tile in tiles.
     *  @return distance from c to tile in tiles, rounded down */
    public static float getDistanceInTiles(Creature c, Tile tile) {
        return getDistanceInTiles(c, tile.getMiddlePos());
    }
    
    /** Calculates the squared distance from Creature c to Tile tile in tiles.
     *  @return squared distance from c to tile in tiles, rounded down */
    public static float getDistance2InTiles(Creature c, Tile tile) {
        return getDistance2InTiles(c, tile.getMiddlePos());
    }
    
    private Math() {}
}
