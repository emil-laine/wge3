package wge3.game.entity.tilelayers.mapobjects;

import static com.badlogic.gdx.math.MathUtils.random;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import wge3.game.engine.constants.Color;
import wge3.game.engine.constants.Statistic;
import static wge3.game.engine.constants.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.game.engine.constants.TilePropertyFlag.IS_PASSABLE;
import wge3.game.entity.Tile;
import wge3.game.entity.creatures.Creature;
import wge3.game.entity.creatures.Player;
import wge3.game.entity.tilelayers.MapObject;

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
