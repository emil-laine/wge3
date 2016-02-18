/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.objects;

import java.util.EnumSet;
import static wge3.model.TilePropertyFlag.BLOCKS_VISION;
import static wge3.model.TilePropertyFlag.CASTS_SHADOWS;
import static wge3.model.TilePropertyFlag.COVERS_WHOLE_TILE;
import wge3.model.MapObject;

public abstract class Wall extends MapObject {
    
    public Wall() {
        propertyFlags = EnumSet.of(BLOCKS_VISION, CASTS_SHADOWS, COVERS_WHOLE_TILE);
    }
}
