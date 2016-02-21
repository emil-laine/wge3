/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.objects;

import static com.badlogic.gdx.math.MathUtils.random;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import wge3.engine.Statistic;
import wge3.engine.util.Color;
import static wge3.engine.util.Color.BLUE;
import wge3.model.Creature;
import wge3.model.MapObject;
import wge3.model.Player;
import wge3.model.Tile;
import static wge3.model.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.model.TilePropertyFlag.IS_PASSABLE;

public class Teleport extends MapObject {
    
    private Color color;
    
    public Teleport(Color color) {
        propertyFlags = EnumSet.of(IS_PASSABLE, CASTS_SHADOWS);
        shadowDepth = 0.75f;
        this.color = color;
        int x;
        switch (color) {
            case RED:   x = 2; break;
            case BLUE:  x = 3; break;
            case GREEN: x = 4; break;
            default:    x = 5; break;
        }
        setSprite(x, 4);
    }
    
    public void teleport(Creature teleporter) {
        List<Teleport> destinations = getTeleportDestinations();
        if (destinations.isEmpty()) return;
        Teleport destination = destinations.get(random(destinations.size()-1));
        teleporter.setPosition(destination.getX(), destination.getY());
        
        if (teleporter.isPlayer()) {
            Player.statistics.addStatToPlayer((Player) teleporter, Statistic.TELEPORTERSUSED, 1);
        }
    }
    
    public Color getColor() {
        return color;
    }
    
    public List<Teleport> getTeleportDestinations() {
        List<Teleport> teleports = new ArrayList();
        // This only happens when somebody uses a teleport
        // so looping through every tile is not a problem.
        for (Tile tile : Tile.getArea().getTiles()) {
            if (tile.hasTeleport()) {
                Teleport tele = (Teleport) tile.getObject();
                if (tele.getColor() == this.color)
                    teleports.add(tele);
            }
        }
        teleports.remove(this);
        return teleports;
    }
}
