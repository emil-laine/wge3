/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.math.Rectangle;

public interface QuadTreeElement {
    public float getLeftX();
    public float getBottomY();
    public float getRightX();
    public float getTopY();
    public float getWidth();
    public float getHeight();
    public Rectangle getBounds();
}
