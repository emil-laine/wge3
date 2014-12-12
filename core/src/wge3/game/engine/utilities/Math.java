package wge3.game.engine.utilities;

import static com.badlogic.gdx.math.MathUtils.PI;
import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.floor;
import static java.lang.Math.sqrt;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;

public final class Math {
    
    public static float angle(float x1, float y1, float x2, float y2) {
        float angle = atan2(y2 - y1, x2 - x1);
        if (angle < 0) angle += PI2;
        return angle;
    }

    public static float getDiff(float src, float dst) {
        float diff = dst - src;
        if (diff > PI) diff -= PI2;
        else if (diff < -PI) diff += PI2;
        return diff;
    }
    
    public static int floatPosToTilePos(float pos) {
        return floor(pos) / Tile.size;
    }
    
    public static boolean isInCenterOfATile(Creature c) {
        return isInCenterOfATile(c.getX(), c.getY());
    }
    
    public static boolean isInCenterOfATile(float x, float y) {
        return isInCenterOfATile(x, y, 0.25f);
    }

    public static boolean isInCenterOfATile(float x, float y, float border) {
        x = (x % Tile.size) / Tile.size;
        y = (y % Tile.size) / Tile.size;
        float opposite = 1f - border;
        return (x >= border && x < opposite) && (y >= border && y < opposite);
    }
    
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) sqrt(getDistance2(x1, y1, x2, y2));
    }
    
    public static float getDistance2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (dx * dx) + (dy * dy);
    }
    
    public static float getDistance(Creature c, float x, float y) {
        return getDistance(c.getX(), c.getY(), x, y);
    }
    
    public static float getDistance2(Creature c, float x, float y) {
        return getDistance2(c.getX(), c.getY(), x, y);
    }
    
    public static float getDistance(Creature c1, Creature c2) {
        return getDistance(c1.getX(), c1.getY(), c2.getX(), c2.getY());
    }
    
    public static float getDistance2(Creature c1, Creature c2) {
        return getDistance2(c1.getX(), c1.getY(), c2.getX(), c2.getY());
    }
    
    public static float getDistanceInTiles(float x1, float y1, float x2, float y2) {
        return getDistance(x1, y1, x2, y2) / Tile.size;
    }
    
    public static float getDistanceInTiles(Creature c, float x, float y) {
        return getDistanceInTiles(c.getX(), c.getY(), x, y);
    }
    
    public static float getDistance2InTiles(Creature c, float x, float y) {
        return getDistance2(c, x, y) / Tile.size;
    }
    
    public static float getDistanceInTiles(Creature c, Tile tile) {
        return getDistanceInTiles(c, tile.getMiddleX(), tile.getMiddleY());
    }
    
    public static float getDistance2InTiles(Creature c, Tile tile) {
        return getDistance2InTiles(c, tile.getMiddleX(), tile.getMiddleY());
    }
}
