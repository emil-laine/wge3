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
    
    
    public static List<Tile> findPath(Tile startingTile, Tile endTile) {
        
        
        
        List<TileData> tiles = new ArrayList<TileData>();
        Area area = endTile.getArea();
        //THIS IS THE QUEUE
        List<TileData> queue = new ArrayList<TileData>();
        queue.add(new TileData(endTile.getX(), endTile.getY(), endTile.isGoodMoveDest()));
        
        //THIS IS THE LOOP FOR THE QUEUE
        for (ListIterator<TileData> it = queue.listIterator(); it.hasNext();) {

            TileData nextTile = it.next();
            
            if (nextTile.getX() == startingTile.getX() && nextTile.getY() == startingTile.getY() ) {
                break;
            }
            
            int x = nextTile.getX();
            int y = nextTile.getY();
            int counter = nextTile.getCounter()+1;
            
            //create shortlist for eliminating walls
            TileData[] list = new TileData[4];
            if (area.hasLocation(x, y+1))
                list[0] = new TileData(x, y+1, counter, area.getTileAt(x, y+1).isGoodMoveDest());
            if (area.hasLocation(x+1, y))
                list[1] = new TileData(x+1, y, counter, area.getTileAt(x+1, y).isGoodMoveDest());
            if (area.hasLocation(x, y-1))
                list[2] = new TileData(x, y-1, counter, area.getTileAt(x, y-1).isGoodMoveDest());
            if (area.hasLocation(x-1, y))
                list[3] = new TileData(x-1, y, counter, area.getTileAt(x-1, y).isGoodMoveDest());
            
            //add only passable objects
            for (int i = 0; i < 4; i++) {
                if (list[i] != null && list[i].isPassable()) {
                    it.add(list[i]);
                }
            } 
        }
        int lastCounter = queue.get(queue.size()-1).getCounter();
        
        List<TileData> moveList = new ArrayList<TileData>();
        TileData currentTileData = new TileData(endTile.getX(), endTile.getY(), true);
        
        for (int i = lastCounter; i > 0; i--) {
            List<TileData> shortlist = new ArrayList<TileData>();
            for (TileData tile : queue) {
                
                
                int nextCounter = tile.getCounter();
                
                if (nextCounter == i && currentTileData.isNextTo(tile)) {
                    shortlist.add(tile);
                }
            } 
            moveList.add(shortlist.get(random(shortlist.size()-1)));
          
            
        }
        
        List<Tile> finalList = new ArrayList<Tile>();
        Tile currentTile = endTile;
        
        for (int i = 1; i < moveList.size(); i++) {
            
            Tile tile = area.getTileAt(moveList.get(i).getX(), moveList.get(i).getY());
            
            
            if (!tile.isAnOKMoveDestinationFrom(currentTile.getX(), currentTile.getY())) {
                finalList.add(area.getTileAt(moveList.get(i-1).getX(), moveList.get(i-1).getY()));
                
                currentTile = area.getTileAt(moveList.get(i-1).getX(), moveList.get(i-1).getY());
            }
        }
        finalList.add(startingTile);
        
        for (Tile tile : finalList) {
            System.out.println("x: " + tile.getX() + " y: " + tile.getY());
            
        }
        System.out.println("asdasdasdasdasdasdasdasdasdasd");
        return finalList;
    }
    
    
    
}
