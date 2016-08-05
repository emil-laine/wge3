/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.components;

import com.badlogic.gdx.math.MathUtils;
import java.util.List;
import wge3.model.Component;
import wge3.model.MapObject;
import wge3.model.Tile;

@SuppressWarnings("unused") // used via reflection
public class Expander extends Component {
    
    private final float expansionRate = 0.02f; // TODO: Move to config file.
    
    @Override
    public void update() {
        if (MathUtils.randomBoolean(expansionRate))
            expand();
    }
    
    private void expand() {
        Tile target = getExpansionTarget();
        if (target == null) return;
        target.setObject(new MapObject("greenSlime"));
    }
    
    private Tile getExpansionTarget() {
        List<Tile> nearbyTiles = getOwner().getTile().getNearbyTiles(false);
        nearbyTiles.removeIf(t -> t.hasCreature() || t.hasObject());
        if (nearbyTiles.isEmpty()) return null;
        return nearbyTiles.get(MathUtils.random(nearbyTiles.size()-1));
    }
}
