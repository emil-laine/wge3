package wge3.game;

import com.badlogic.gdx.graphics.g2d.Batch;

////////////////////////////////////////////
// All game objects that are drawn on the //
// screen must implement this interface.  //
////////////////////////////////////////////

public interface Drawable {
    public void draw(Batch batch);
}
