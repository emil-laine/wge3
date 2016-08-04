/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.components;

import java.util.ArrayList;
import java.util.List;
import wge3.engine.Statistic;
import wge3.model.Component;
import wge3.model.Entity;
import wge3.model.Player;
import wge3.model.Tile;
import static com.badlogic.gdx.math.MathUtils.random;
import wge3.engine.util.Color;

public class Teleport extends Component {
    
    private Color color;
    
    public Teleport(/*Object... params*/) {
        //getOwner().setSprite(0, 0);
    }
    
    @Override
    public void entityTouches(Entity toucher) {
        teleportEntity(toucher);
    }
    
    private void teleportEntity(Entity teleporter) {
        List<Tile> destinations = getTeleportDestinations();
        Tile destination;
        
        if (destinations.isEmpty()) {
            destination = teleporter.getArea().getRandomTileWithoutObject();
        } else {
            destination = destinations.get(random(destinations.size()-1));
        }
        teleporter.setPosition(destination.getMiddleX(), destination.getMiddleY());
        
        if (teleporter.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) teleporter, Statistic.TELEPORTS_USED, 1);
        }
    }
    
    private List<Tile> getTeleportDestinations() {
        List<Tile> destinations = new ArrayList();
        for (Tile tile : Tile.getArea().getTiles()) {
            if (tile == getOwner().getTile() || !tile.hasObject())
                continue;
            
            Teleport teleportComponent = tile.getObject().getComponent(Teleport.class);
            if (teleportComponent == null)
                continue;
            if (teleportComponent.color == color)
                destinations.add(tile);
        }
        return destinations;
    }
}

//public class Teleport extends MapObject {
//    
//    private Color color;
//    
//    public Teleport(Color color) {
//        this.color = color;
//        int x;
//        switch (color) {
//            case RED:   x = 2; break;
//            case BLUE:  x = 3; break;
//            case GREEN: x = 4; break;
//            default:    x = 5; break;
//        }
//        setSprite(x, 4);
//    }
//    
//    public Color getColor() {
//        return color;
//    }
