package wge3.game.entity.tilelayers.mapobjects;

import wge3.game.entity.tilelayers.MapObject;

public class Tree extends MapObject {

    public Tree() {
        setSprite(6, 1);
        passable = false;
        blocksVision = false;
        coversWholeTile = false;
        castsShadows = true;
        shadowDepth = 0.75f;
    }
}
