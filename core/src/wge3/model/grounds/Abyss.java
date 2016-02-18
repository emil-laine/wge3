/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.grounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.EnumSet;
import static wge3.model.TilePropertyFlag.DRAINS_HP;
import wge3.model.Ground;

public final class Abyss extends Ground {
    
    public Abyss() {
        propertyFlags = EnumSet.of(DRAINS_HP);
        HPDrainAmount = 1000;
    }
    
    @Override
    public void draw(Batch batch) {
    }
    
    @Override
    public void setPosition(int x, int y) {
    }
    
    @Override
    public void setLighting(Color color) {
    }
}
