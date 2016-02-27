/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.math.MathUtils;

public enum Direction {
    RIGHT,
    UP_RIGHT,
    UP,
    UP_LEFT,
    LEFT,
    DOWN_LEFT,
    DOWN,
    DOWN_RIGHT;
    
    public float getDx() {
        return MathUtils.cos(getAngleRad());
    }
    
    public float getDy() {
        return MathUtils.sin(getAngleRad());
    }
    
    public float getAngleRad() {
        return ordinal() / 8f * MathUtils.PI2;
    }
}
