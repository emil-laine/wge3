package wge3.interfaces;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

////////////////////////////////////////////
// All game objects that are drawn on the //
// screen must implement this interface.  //
//                                        //
// In addition to these two methods, all  //
// Drawables need a private or protected  //
// boolean named needsToBeDrawn, the      //
// value of which needsToBeDrawn() will   //
// return.                                //
////////////////////////////////////////////

// This shouldn't probably be an interface, because
// needsToBeDrawn() will always have the same body,
// and all Drawables need to have a private boolean
// called needsToBeDrawn, which can't be defined
// in an interface.

public interface Drawable {
    public void draw(ShapeRenderer sr);
    // This might be useful too:
    // public void forceDraw(ShapeRenderer sr);
    public boolean needsToBeDrawn();
}
