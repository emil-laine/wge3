/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.grounds;

import wge3.model.Ground;

public final class Water extends Ground {
    
    public Water() {
        setSprite(2, 0);
        movementModifier = 0.5f;
    }
}
