/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.graphics.g2d.Batch;

////////////////////////////////////////////
// All game objects that are drawn on the //
// screen must implement this interface.  //
////////////////////////////////////////////

public interface Drawable {
    public void draw(Batch batch);
}
