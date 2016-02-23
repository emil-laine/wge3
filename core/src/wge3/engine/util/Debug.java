/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class Debug {
    
    public static final boolean aiDebug = false;
    public static final boolean moveTaskDebug = false;
    public static final boolean quadTreeDebug = true;
    
    public static ShapeRenderer getShapeRenderer() {
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        return shapeRenderer;
    }
    
    public static BitmapFont getFont() {
        if (font == null) {
            font = new BitmapFont();
        }
        return font;
    }
    
    private static ShapeRenderer shapeRenderer;
    private static BitmapFont font;
    
    private Debug() {}
}
