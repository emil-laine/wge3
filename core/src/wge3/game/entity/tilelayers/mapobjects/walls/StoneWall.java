package wge3.game.entity.tilelayers.mapobjects.walls;

import wge3.game.entity.tilelayers.mapobjects.Wall;

public final class StoneWall extends Wall {

    public StoneWall(int type) {
        if (type < 0 || type > 2) throw new IllegalArgumentException();
        setSprite(3+type, 1);
    }

    @Override
    public void dealDamage(int amount) {
    }
}
